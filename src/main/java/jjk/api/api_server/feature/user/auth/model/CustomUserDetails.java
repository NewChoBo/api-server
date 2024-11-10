package jjk.api.api_server.feature.user.auth.model;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUserDetails implements UserDetails {


  private final Long id;
  private final String username;
  private final String password;
  private final Collection<? extends GrantedAuthority> authorities;

  public CustomUserDetails(long id, String username,
      Collection<? extends GrantedAuthority> authorities) {
    this(id, username, null, authorities);
  }

  public CustomUserDetails(Long id, String username, String password,
      Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.authorities = authorities;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}