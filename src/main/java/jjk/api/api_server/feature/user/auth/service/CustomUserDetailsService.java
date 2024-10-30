package jjk.api.api_server.feature.user.auth.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import jjk.api.api_server.common.util.JwtUtil;
import jjk.api.api_server.feature.user.auth.dto.LoginDto;
import jjk.api.api_server.feature.user.auth.model.CustomUserDetails;
import jjk.api.api_server.feature.user.user.dto.RoleDto;
import jjk.api.api_server.feature.user.user.dto.UserDto;
import jjk.api.api_server.feature.user.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

  private final UserService userService;
  private final ModelMapper modelMapper;
  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;

  public CustomUserDetailsService(UserService userService, ModelMapper modelMapper,
      AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
    this.userService = userService;
    this.modelMapper = modelMapper;
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
  }

  public ResponseEntity<String> signIn(@RequestBody LoginDto loginDto) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
    } catch (AuthenticationException e) {
      log.error("Invalid username or password", e);
      return ResponseEntity.status(401).body("Invalid username or password");
    }

    final CustomUserDetails userDetails = this.loadUserByUsername(loginDto.getUsername());
    final String jwt = jwtUtil.generateToken(userDetails);

    return ResponseEntity.ok(jwt);
  }

  public void signUp(LoginDto loginDto) {
    UserDto userDto = modelMapper.map(loginDto, UserDto.class);
    userService.createUser(userDto);
  }

  @Override
  public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<UserDto> optionalUserDto = userService.findByUsername(username);

    if (optionalUserDto.isEmpty()) {
      throw new UsernameNotFoundException("User not found");
    }

    UserDto userDto = optionalUserDto.get();

    Set<GrantedAuthority> grantedAuthorities = getAuthByRoles(userDto.getRoleDto());
    return new CustomUserDetails(userDto.getLoginId(), userDto.getPassword(), grantedAuthorities);
  }

  Set<GrantedAuthority> getAuthByRoles(Set<RoleDto> roles) {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toSet());
  }
}