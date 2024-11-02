package jjk.api.api_server.common.init;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
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

    User admin = User.builder()
        .loginId("admin")
        .username("관리자 이름")
        .password(encodedPassword)
        .email("abcd@efg.com")
        .createdDate(LocalDateTime.now())
        .updatedDate(LocalDateTime.now())
        .roles(new HashSet<>())
        .build();
    userRepository.save(admin);
    User user = User.builder()
        .loginId("user")
        .username("일반 이용자")
        .password(encodedPassword)
        .email("abcd2@efg.com")
        .createdDate(LocalDateTime.now())
        .updatedDate(LocalDateTime.now())
        .roles(new HashSet<>())
        .build();
    userRepository.save(user);
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

    admin.getRoles().add(roleUser);
    admin.getRoles().add(roleAdmin);
    user.getRoles().add(roleAdmin);

    // 매핑 테이블을 저장
    userRepository.save(admin);
    userRepository.save(user);
  }
}
