package jjk.api.api_server.feature.user.user.service;

import java.util.List;
import java.util.Optional;
import jjk.api.api_server.feature.user.user.entity.User;
import jjk.api.api_server.feature.user.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // 사용자 생성
  public void createUser(User user) {
    userRepository.save(user);
  }

  // 모든 사용자 조회
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  // ID로 사용자 조회
  public Optional<User> getUserById(Long id) {
    return userRepository.findById(id);
  }

  // 사용자 업데이트
  public Optional<User> updateUser(Long id, User userDetails) {
    return userRepository.findById(id).map(user -> {
      user.setUsername(userDetails.getUsername());
      user.setEmail(userDetails.getEmail());
      user.setUpdatedDate(userDetails.getUpdatedDate());
      return userRepository.save(user);
    });
  }

  // 사용자 삭제
  public boolean deleteUser(Long id) {
    if (userRepository.existsById(id)) {
      userRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
