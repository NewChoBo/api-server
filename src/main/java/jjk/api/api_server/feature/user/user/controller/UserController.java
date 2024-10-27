package jjk.api.api_server.feature.user.user.controller;

import java.util.List;
import jjk.api.api_server.feature.user.user.dto.UserDto;
import jjk.api.api_server.feature.user.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  // 사용자 생성
  @PostMapping
  public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
    userService.createUser(userDto);
    return new ResponseEntity<>(userDto, HttpStatus.OK);
  }

  // 모든 사용자 조회
  @GetMapping
  public ResponseEntity<List<UserDto>> getAllUsers() {
    List<UserDto> userDtos = userService.getAllUsers();
    return new ResponseEntity<>(userDtos, HttpStatus.OK);
  }

  // ID로 사용자 조회
  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
    UserDto user = userService.getUserById(id);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  // 사용자 업데이트
  @PutMapping("/{id}")
  public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
    userService.updateUser(id, userDto);
    return null;
  }

  // 사용자 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    boolean isDeleted = userService.deleteUser(id);
    return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
}
