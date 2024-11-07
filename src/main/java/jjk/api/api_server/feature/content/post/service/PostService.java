package jjk.api.api_server.feature.content.post.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import jjk.api.api_server.common.dto.ListDto;
import jjk.api.api_server.feature.content.post.dto.PostDto;
import jjk.api.api_server.feature.content.post.dto.SearchDto;
import jjk.api.api_server.feature.content.post.entity.Post;
import jjk.api.api_server.feature.content.post.entity.QPost;
import jjk.api.api_server.feature.content.post.repository.PostRepository;
import jjk.api.api_server.feature.user.user.dto.UserDto;
import jjk.api.api_server.feature.user.user.entity.QUser;
import jjk.api.api_server.feature.user.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PostService {

  private final ModelMapper modelMapper;
  private final JPAQueryFactory jpaQueryFactory;
  private final PostRepository postRepository;

  // QEntity
  private static final QUser qUser = QUser.user;
  private static final QPost qPost = QPost.post;

  public PostService(ModelMapper modelMapper, JPAQueryFactory jpaQueryFactory,
      PostRepository postRepository) {
    this.modelMapper = modelMapper;
    this.jpaQueryFactory = jpaQueryFactory;
    this.postRepository = postRepository;
  }

  @Transactional
  public void createPost(PostDto postDto, String userId) {
    User user = fetchUserByLoginId(userId);
    Post post = modelMapper.map(postDto, Post.class);
    post.setUser(user);
    postRepository.save(post);
  }

  @Transactional(readOnly = true)
  public ListDto<PostDto> getPostList(SearchDto searchDto) {
    log.info(searchDto.toString());
    List<Post> posts = jpaQueryFactory.selectFrom(qPost).fetch();
    Long size = jpaQueryFactory.select(qPost.count()).from(qPost).fetchOne();

    List<PostDto> postDtos = posts.stream()
        .map(post -> modelMapper.map(post, PostDto.class))
        .toList();

    return ListDto.<PostDto>builder()
        .items(postDtos)
        .totalCount(size)
        .build();
  }

  @Transactional(readOnly = true)
  public PostDto getPost(Long postId) {
    return jpaQueryFactory.select(
            Projections.bean(PostDto.class, qPost.id, qPost.title, qPost.contents,
                Projections.bean(UserDto.class, qPost.user.loginId).as("user"))).from(qPost)
        .leftJoin(qPost.user, qUser).where(qPost.id.eq(postId)).fetchOne();
  }

  @Transactional
  public void updatePost(PostDto postDto) {
    Post post = postRepository.findById(postDto.getId())
        .orElseThrow(
            () -> new EntityNotFoundException("Post not found with id: " + postDto.getId()));
    post.setTitle(postDto.getTitle());
    post.setContents(postDto.getContents());
    postRepository.save(post);
  }

  @Transactional
  public void deletePost(Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));
    postRepository.delete(post);
  }

  private User fetchUserByLoginId(String userId) {
    User user = jpaQueryFactory.select(Projections.fields(User.class, qUser.id))
        .from(qUser)
        .where(qUser.loginId.eq(userId))
        .fetchFirst();

    if (user == null) {
      throw new EntityNotFoundException("User not found with loginId: " + userId);
    }
    return user;
  }
}