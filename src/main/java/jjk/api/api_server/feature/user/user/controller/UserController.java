package jjk.api.api_server.feature.user.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import jjk.api.api_server.feature.user.user.dto.UserDto;
import jjk.api.api_server.feature.user.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "사용자 정보 관리가 가능 API")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  // 사용자 생성
  @PostMapping
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Operation(summary = "Create a new user", description = "Creates a new user with the provided details")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully created user", content = @Content(schema = @Schema(implementation = UserDto.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
  })
  public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
    userService.createUser(userDto);
    return new ResponseEntity<>(userDto, HttpStatus.OK);
  }

  // 모든 사용자 조회
  @GetMapping
  @PreAuthorize("hasRole('ROLE_USER')")
  @Operation(summary = "Get all users", description = "Retrieves a list of all users")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved list", content = @Content(schema = @Schema(implementation = List.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
      @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
      @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
  })
  public ResponseEntity<List<UserDto>> getAllUsers() {
    List<UserDto> userDtos = userService.getAllUsers();
    return new ResponseEntity<>(userDtos, HttpStatus.OK);
  }

  // ID로 사용자 조회
  @GetMapping("/{id}")
  @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved user", content = @Content(schema = @Schema(implementation = UserDto.class))),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  })
  public ResponseEntity<Optional<UserDto>> getUserById(@PathVariable Long id) {
    Optional<UserDto> user = userService.getUserById(id);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  // 사용자 업데이트
  @PutMapping("/{id}")
  @Operation(summary = "Update an existing user", description = "Updates an existing user with the provided details")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully updated user", content = @Content(schema = @Schema(implementation = UserDto.class))),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  })
  public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
    userService.updateUser(id, userDto);
    return null;
  }

  // 사용자 삭제
  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a user", description = "Deletes a user by their ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successfully deleted user", content = @Content),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  })
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    boolean isDeleted = userService.deleteUser(id);
    return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
}
