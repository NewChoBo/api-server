package jjk.api.api_server.feature.user.auth.controller;

import jjk.api.api_server.common.util.JwtUtil;
import jjk.api.api_server.feature.user.auth.dto.LoginDto;
import jjk.api.api_server.feature.user.auth.model.CustomUserDetails;
import jjk.api.api_server.feature.user.auth.service.CustomUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class CustomUserController {

  private final AuthenticationManager authenticationManager;
  private final CustomUserDetailsService userService;
  private final JwtUtil jwtUtil;

  public CustomUserController(AuthenticationManager authenticationManager,
      CustomUserDetailsService userService, JwtUtil jwtUtil) {
    this.authenticationManager = authenticationManager;
    this.userService = userService;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/signin")
  public ResponseEntity<String> signIn(@RequestBody LoginDto loginDto) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
      );
    } catch (AuthenticationException e) {
      e.printStackTrace();
      return ResponseEntity.status(401).body("Invalid username or password");
    }

    final CustomUserDetails userDetails = userService.loadUserByUsername(loginDto.getUsername());
    final String jwt = jwtUtil.generateToken(userDetails);

    return ResponseEntity.ok(jwt);
  }

}