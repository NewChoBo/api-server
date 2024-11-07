package jjk.api.api_server.feature.user.user.repository;

import java.util.Optional;
import jjk.api.api_server.feature.user.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByName(String name);
}