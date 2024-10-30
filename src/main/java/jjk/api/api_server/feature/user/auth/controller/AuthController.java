package jjk.api.api_server.feature.user.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jjk.api.api_server.feature.user.auth.dto.LoginDto;
import jjk.api.api_server.feature.user.auth.service.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private static final Logger log = LoggerFactory.getLogger(AuthController.class);

  private final CustomUserDetailsService userService;

  public AuthController(CustomUserDetailsService userService) {
    this.userService = userService;
  }

  @PostMapping("/signin")
  @Operation(summary = "Sign in", description = "Authenticate user and return JWT token")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully authenticated", content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "401", description = "Invalid username or password", content = @Content)})
  public ResponseEntity<String> signIn(@RequestBody LoginDto loginDto) {
    return userService.signIn(loginDto);
  }

  @PostMapping("/signup")
  public ResponseEntity<String> signUp(@RequestBody LoginDto loginDto) {
    try {
      userService.signUp(loginDto);
      return ResponseEntity.ok("Sign up success");
    } catch (Exception e) {
      log.error("Failed to sign up", e);
      return ResponseEntity.status(400).body("Failed to sign up");
    }
  }

}