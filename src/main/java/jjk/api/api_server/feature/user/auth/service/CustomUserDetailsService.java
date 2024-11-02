package jjk.api.api_server.feature.user.auth.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import jjk.api.api_server.feature.user.auth.model.CustomUserDetails;
import jjk.api.api_server.feature.user.user.dto.RoleDto;
import jjk.api.api_server.feature.user.user.dto.UserDto;
import jjk.api.api_server.feature.user.user.entity.QRole;
import jjk.api.api_server.feature.user.user.entity.QUser;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final AuthService authService;
  private final JPAQueryFactory jpaQueryFactory;

  public CustomUserDetailsService(@Lazy AuthService authService, JPAQueryFactory jpaQueryFactory) {
    this.authService = authService;
    this.jpaQueryFactory = jpaQueryFactory;
  }

  @Override
  public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    QUser qUser = QUser.user;
    QRole qRole = QRole.role;
    UserDto userDto = jpaQueryFactory.select(Projections.bean(UserDto.class,
            qUser.id,
            qUser.username,
            qUser.loginId,
            qUser.password,
            qUser.email,
            qUser.createdDate,
            qUser.updatedDate,
            Projections.list(Projections.bean(RoleDto.class, qRole.id, qRole.name, qRole.memo))))
        .from(qUser)
        .leftJoin(qUser.roles, qRole)
        .fetchJoin()
        .where(qUser.loginId.eq(username))
        .fetchOne();

    System.out.println("userDto = " + userDto);

    if (userDto == null) {
      throw new UsernameNotFoundException("User not found");
    }

    List<SimpleGrantedAuthority> grantedAuthorities = getAuthByRoles(userDto.getRoles());
    return new CustomUserDetails(userDto.getLoginId(), userDto.getPassword(), grantedAuthorities);
  }

  List<SimpleGrantedAuthority> getAuthByRoles(List<RoleDto> roles) {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .toList();
  }
}