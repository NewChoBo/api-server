package jjk.api.api_server.common.init;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import jjk.api.api_server.common.util.PasswordUtil;
import jjk.api.api_server.feature.user.auth.repository.RoleRepository;
import jjk.api.api_server.feature.user.user.entity.Role;
import jjk.api.api_server.feature.user.user.entity.User;
import jjk.api.api_server.feature.user.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseInitializer {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  @Autowired
  public DatabaseInitializer(UserRepository userRepository, RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }

  @PostConstruct
  @Transactional
  public void init() {
    if (userRepository.count() == 0 && roleRepository.count() == 0) {
      initUserAndRole();
    }
  }

  private void initUserAndRole() {
    String encodedPassword = PasswordUtil.encodePassword("password");

    User user1 = User.builder()
        .loginId("user1")
        .username("user name1")
        .password(encodedPassword)
        .email("abcd@efg.com")
        .createdDate(LocalDateTime.now())
        .updatedDate(LocalDateTime.now())
        .roles(new ArrayList<>())
        .build();
    userRepository.save(user1);
    User user2 = User.builder()
        .loginId("user2")
        .username("user name2")
        .password(encodedPassword)
        .email("abcd2@efg.com")
        .createdDate(LocalDateTime.now())
        .updatedDate(LocalDateTime.now())
        .roles(new ArrayList<>())
        .build();
    userRepository.save(user2);
    Role roleUser = Role.builder()
        .name("ROLE_USER")
        .memo("Standard user role")
        .build();
    roleRepository.save(roleUser);

    Role roleAdmin = Role.builder()
        .name("ROLE_ADMIN")
        .memo("Administrator role")
        .build();
    roleRepository.save(roleAdmin);

    user1.getRoles().add(roleUser);
    user2.getRoles().add(roleAdmin);

    // 매핑 테이블을 저장
    userRepository.save(user1);
    userRepository.save(user2);
  }
}
