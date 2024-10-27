package jjk.api.api_server.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
        .authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll() // 모든 경로 허용
        ).httpBasic(AbstractHttpConfigurer::disable) // HTTP 기본 인증 비활성화
        .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 비활성화
        .logout(AbstractHttpConfigurer::disable); // 로그아웃 비활성화

    return http.build();
  }
}
