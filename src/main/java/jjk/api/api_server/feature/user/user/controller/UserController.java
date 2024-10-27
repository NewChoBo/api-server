package jjk.api.api_server.feature.user.user.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import jjk.api.api_server.feature.user.user.dto.UserDto;
import jjk.api.api_server.feature.user.user.entity.User;
import jjk.api.api_server.feature.user.user.service.UserService;
import org.modelmapper.ModelMapper;
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
  private final ModelMapper modelMapper;

  public UserController(UserService userService, ModelMapper modelMapper) {
    this.userService = userService;
    this.modelMapper = modelMapper;
  }

  // 사용자 생성
  @PostMapping
  public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
    userService.createUser(modelMapper.map(userDto, User.class));
    return new ResponseEntity<>(userDto, HttpStatus.OK);
  }

  // 모든 사용자 조회
  @GetMapping
  public ResponseEntity<List<UserDto>> getAllUsers() {
    List<User> users = userService.getAllUsers();
    List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class))
        .collect(Collectors.toList());
    return new ResponseEntity<>(userDtos, HttpStatus.OK);
  }

  // ID로 사용자 조회
  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
    Optional<User> user = userService.getUserById(id);
    return user.map(
            value -> new ResponseEntity<>(modelMapper.map(value, UserDto.class), HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  // 사용자 업데이트
  @PutMapping("/{id}")
  public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
    User userDetails = modelMapper.map(userDto, User.class);
    Optional<User> updatedUser = userService.updateUser(id, userDetails);
    return updatedUser.map(
            value -> new ResponseEntity<>(modelMapper.map(value, UserDto.class), HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  // 사용자 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    boolean isDeleted = userService.deleteUser(id);
    return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
}
