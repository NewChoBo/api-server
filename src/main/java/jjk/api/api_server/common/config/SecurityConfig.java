package jjk.api.api_server.common.config;

import jjk.api.api_server.common.filter.JwtRequestFilter;
import jjk.api.api_server.feature.user.auth.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final CustomUserDetailsService customUserDetailsService;
  private final JwtRequestFilter jwtRequestFilter;

  public SecurityConfig(CustomUserDetailsService customUserDetailsService,
      JwtRequestFilter jwtRequestFilter) {
    this.customUserDetailsService = customUserDetailsService;
    this.jwtRequestFilter = jwtRequestFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable) // CSRF protection disabled
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/api/users/**").authenticated() // Secure API endpoints
            .anyRequest().permitAll() // Allow all other requests
        )
        .sessionManagement(session -> session.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS)) // Stateless session management
        .httpBasic(AbstractHttpConfigurer::disable) // Disable HTTP Basic authentication
        .formLogin(AbstractHttpConfigurer::disable) // Disable form login
        .logout(AbstractHttpConfigurer::disable); // Disable logout

    http.addFilterBefore(jwtRequestFilter,
        UsernamePasswordAuthenticationFilter.class); // Add JWT filter

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
    auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    return auth.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}