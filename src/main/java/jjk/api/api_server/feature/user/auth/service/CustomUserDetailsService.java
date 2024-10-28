package jjk.api.api_server.feature.user.auth.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import jjk.api.api_server.feature.user.auth.model.CustomUserDetails;
import jjk.api.api_server.feature.user.user.dto.RoleDto;
import jjk.api.api_server.feature.user.user.dto.UserDto;
import jjk.api.api_server.feature.user.user.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserService userService;

  public CustomUserDetailsService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<UserDto> optionalUserDto = userService.findByUsername(username);

    if (optionalUserDto.isEmpty()) {
      throw new UsernameNotFoundException("User not found");
    }

    UserDto userDto = optionalUserDto.get();

    Set<GrantedAuthority> grantedAuthorities = getAuthByRoles(userDto.getRoleDto());
    return new CustomUserDetails(userDto.getUsername(), userDto.getPassword(), grantedAuthorities);
  }

  Set<GrantedAuthority> getAuthByRoles(Set<RoleDto> roles) {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toSet());
  }
}