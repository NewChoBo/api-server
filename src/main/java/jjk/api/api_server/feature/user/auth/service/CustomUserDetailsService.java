package jjk.api.api_server.feature.user.auth.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import jjk.api.api_server.feature.user.auth.model.CustomUserDetails;
import jjk.api.api_server.feature.user.user.dto.RoleDto;
import jjk.api.api_server.feature.user.user.dto.UserDto;
import jjk.api.api_server.feature.user.user.entity.User;
import jjk.api.api_server.feature.user.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {


  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  public CustomUserDetailsService(UserRepository userRepository, ModelMapper modelMapper) {
    this.userRepository = userRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public CustomUserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
    Optional<User> optionalUser = userRepository.findByLoginId(loginId);
    UserDto userDto = modelMapper.map(
        optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found")),
        UserDto.class);

    Set<GrantedAuthority> grantedAuthorities = getAuthByRoles(userDto.getRole());
    return new CustomUserDetails(userDto.getId(), userDto.getLoginId(), userDto.getPassword(),
        grantedAuthorities);
  }

  Set<GrantedAuthority> getAuthByRoles(Set<RoleDto> roles) {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toSet());
  }
}