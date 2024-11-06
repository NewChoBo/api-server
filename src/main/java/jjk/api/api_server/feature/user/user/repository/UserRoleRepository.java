package jjk.api.api_server.feature.user.user.repository;

import java.util.Optional;
import jjk.api.api_server.feature.user.user.entity.User;
import jjk.api.api_server.feature.user.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

  Optional<User> findByLoginId(String loginId);
}