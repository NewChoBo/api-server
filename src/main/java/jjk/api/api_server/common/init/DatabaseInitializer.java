package jjk.api.api_server.common.init;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import jjk.api.api_server.common.util.PasswordUtil;
import jjk.api.api_server.feature.user.user.entity.Role;
import jjk.api.api_server.feature.user.user.entity.User;
import jjk.api.api_server.feature.user.user.entity.UserRole;
import jjk.api.api_server.feature.user.user.repository.RoleRepository;
import jjk.api.api_server.feature.user.user.repository.UserRepository;
import jjk.api.api_server.feature.user.user.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseInitializer {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final UserRoleRepository userRoleRepository;

  @Autowired
  public DatabaseInitializer(UserRepository userRepository, RoleRepository roleRepository,
      UserRoleRepository userRoleRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.userRoleRepository = userRoleRepository;
  }

  @PostConstruct
  @Transactional
  public void init() {
    if (userRepository.count() == 0) {
      initUsers();
    }
    if (roleRepository.count() == 0) {
      initRoles();
    }
    if (userRoleRepository.count() == 0) {
      initUserRoles();
    }
  }

  private void initUsers() {
    String encodedPassword = PasswordUtil.encodePassword("password");

    User admin = User.builder()
        .loginId("admin")
        .username("관리자 이름")
        .password(encodedPassword)
        .email("abcd@efg.com")
        .createdDate(LocalDateTime.now())
        .updatedDate(LocalDateTime.now())
        .build();
    userRepository.save(admin);

    User user = User.builder()
        .loginId("user")
        .username("일반 이용자")
        .password(encodedPassword)
        .email("abcd2@efg.com")
        .createdDate(LocalDateTime.now())
        .updatedDate(LocalDateTime.now())
        .build();
    userRepository.save(user);
  }

  private void initRoles() {
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
  }

  private void initUserRoles() {
    User admin = userRepository.findByLoginId("admin").orElseThrow();
    User user = userRepository.findByLoginId("user").orElseThrow();
    Role roleUser = roleRepository.findByName("ROLE_USER").orElseThrow();
    Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElseThrow();

    UserRole adminUserRole = UserRole.builder()
        .user(admin)
        .role(roleUser)
        .build();
    userRoleRepository.save(adminUserRole);

    UserRole adminAdminRole = UserRole.builder()
        .user(admin)
        .role(roleAdmin)
        .build();
    userRoleRepository.save(adminAdminRole);

    UserRole userUserRole = UserRole.builder()
        .user(user)
        .role(roleUser)
        .build();
    userRoleRepository.save(userUserRole);
  }
}