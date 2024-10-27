package jjk.api.api_server.feature.user.user.repository;

import jjk.api.api_server.feature.user.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
