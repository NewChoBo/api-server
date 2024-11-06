package jjk.api.api_server.feature.user.user.repository;

import jjk.api.api_server.feature.user.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}