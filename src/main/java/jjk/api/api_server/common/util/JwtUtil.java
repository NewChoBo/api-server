package jjk.api.api_server.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import jjk.api.api_server.feature.user.auth.model.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private long expiration;

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(secret.getBytes());
  }

  public String generateToken(CustomUserDetails user) {
    return Jwts.builder().setSubject(String.valueOf(user.getId()))
        .claim("userId", user.getUsername())
        .claim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
        .setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSigningKey(), SignatureAlgorithm.HS512).compact();
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
        .getBody();
  }

  public boolean validateToken(String token) {
    try {
      // 토큰을 파싱하여 서명 검증을 수행하고 클레임을 추출
      Claims claims = extractAllClaims(token);
      return !isTokenExpired(claims);
    } catch (Exception e) {
      // 토큰이 유효하지 않거나 서명 검증에 실패한 경우 false 반환
      return false;
    }
  }

  private boolean isTokenExpired(Claims claims) {
    return claims.getExpiration().before(new Date());
  }

  public List<String> extractRoles(String token) {
    return extractClaim(token, claims -> {
      List<?> roles = claims.get("roles", List.class);
      return roles.stream()
          .filter(String.class::isInstance)
          .map(String.class::cast)
          .toList();
    });
  }

  public CustomUserDetails extractUserInfo(String token) {
    String username = extractUsername(token);
    List<String> roles = extractRoles(token);

    // GrantedAuthority 목록 생성
    List<GrantedAuthority> authorities = roles.stream()
        .map(role -> (GrantedAuthority) () -> role)
        .toList();

    // CustomUserDetails 객체 생성
    return new CustomUserDetails(username, authorities);
  }

}