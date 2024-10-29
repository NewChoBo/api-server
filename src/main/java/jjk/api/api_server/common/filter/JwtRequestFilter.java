package jjk.api.api_server.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import jjk.api.api_server.common.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);

  private final JwtUtil jwtUtil;

  public JwtRequestFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {
    final String authorizationHeader = request.getHeader("Authorization");

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      String jwt = authorizationHeader.substring(7);

      if (jwtUtil.validateToken(jwt)) {
        // 토큰에서 사용자 이름 및 권한 정보 가져오기
        String username = jwtUtil.extractUsername(jwt); // 사용자 이름 추출
        String role = jwtUtil.extractRole(jwt); // 권한 추출

        // 권한 정보를 포함한 인증 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            username, null, Collections.singletonList(new SimpleGrantedAuthority(role)));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // SecurityContext에 인증 정보 설정
        log.info("User '{}' authenticated", username);
        log.info("role: {}", role);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }
    } else {
      log.info("Authorization header is missing or invalid");
    }
    chain.doFilter(request, response);
  }
}