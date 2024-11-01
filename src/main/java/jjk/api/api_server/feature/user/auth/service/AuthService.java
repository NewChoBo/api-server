package jjk.api.api_server.feature.user.auth.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.Optional;
import jjk.api.api_server.common.util.JwtUtil;
import jjk.api.api_server.feature.user.auth.dto.SignInDto;
import jjk.api.api_server.feature.user.auth.dto.SignUpDto;
import jjk.api.api_server.feature.user.auth.model.CustomUserDetails;
import jjk.api.api_server.feature.user.user.dto.UserDto;
import jjk.api.api_server.feature.user.user.entity.QRole;
import jjk.api.api_server.feature.user.user.entity.QUser;
import jjk.api.api_server.feature.user.user.entity.User;
import jjk.api.api_server.feature.user.user.repository.UserRepository;
import jjk.api.api_server.feature.user.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthService {

  private final UserRepository userRepository;

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final CustomUserDetailsService customUserDetailsService;
  private final PasswordEncoder passwordEncoder;
  private final JPAQueryFactory jpaQueryFactory;
  private final UserService userService;

  public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
      CustomUserDetailsService customUserDetailsService,
      UserRepository userRepository, PasswordEncoder passwordEncoder,
      JPAQueryFactory jpaQueryFactory, UserService userService) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
    this.customUserDetailsService = customUserDetailsService;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jpaQueryFactory = jpaQueryFactory;
    this.userService = userService;
  }

  // QEntity
  QUser qUser = QUser.user;
  QRole qRole = QRole.role;

  @Transactional(readOnly = true)
  public ResponseEntity<String> signIn(SignInDto signInDto) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(signInDto.getLoginId(), signInDto.getPassword()));
    } catch (AuthenticationException e) {
      log.error("Invalid username or password", e);
      return ResponseEntity.status(401).body("Invalid username or password");
    }
    final CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(
        signInDto.getLoginId());
    final String jwt = jwtUtil.generateToken(userDetails);

    return ResponseEntity.ok(jwt);
  }

  @Transactional
  public ResponseEntity<String> signUp(SignUpDto signUpDto) {
    try {
      User user = User.builder()
          .username(signUpDto.getUsername())
          .loginId(signUpDto.getLoginId())
          .password(passwordEncoder.encode(signUpDto.getPassword()))
          .email(signUpDto.getEmail())
          .createdDate(LocalDateTime.now())
          .build();
      userRepository.save(user);
      userRepository.flush();
    } catch (Exception e) {
      log.error("Failed to sign up", e);
      return ResponseEntity.status(500).body("Failed to sign up");
    }
    return ResponseEntity.ok("Signed up successfully");
  }

  public ResponseEntity<String> signOut() {
    // TODO: token 만료 처리 필요
    return ResponseEntity.ok("Signed out successfully");
  }

  @Transactional(readOnly = true)
  public Optional<UserDto> findByUsername(String loginId) {
    User user = jpaQueryFactory.selectFrom(qUser).leftJoin(qUser.roles, qRole)
        .where(qUser.loginId.eq(loginId)).fetchOne();

    return Optional.ofNullable(user).map(userService::userToDto);
  }
}
