package jjk.api.api_server.feature.content.post.repository;

import jjk.api.api_server.feature.content.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
