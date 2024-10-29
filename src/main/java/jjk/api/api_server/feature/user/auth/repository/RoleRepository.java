package jjk.api.api_server.feature.user.auth.repository;

import jjk.api.api_server.feature.user.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {

}