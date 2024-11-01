package jjk.api.api_server.feature.user.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jjk.api.api_server.feature.user.auth.dto.SignInDto;
import jjk.api.api_server.feature.user.auth.dto.SignUpDto;
import jjk.api.api_server.feature.user.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentications", description = "가입 및 로그인/아웃 기능")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/signin")
  public ResponseEntity<String> signIn(@RequestBody SignInDto signInDto) {
    return authService.signIn(signInDto);
  }

  @PostMapping("/signup")
  public ResponseEntity<String> signUp(@RequestBody SignUpDto signUpDto) {
    return authService.signUp(signUpDto);
  }

  @PostMapping("/signout")
  public ResponseEntity<String> signOut() {
    return authService.signOut();
  }
}