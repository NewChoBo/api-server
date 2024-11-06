package jjk.api.api_server.feature.content.post.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

  // QEntity
  private static final QUser qUser = QUser.user;
  private static final QPost qPost = QPost.post;
  private final PostRepository postRepository;

  public PostService(ModelMapper modelMapper, JPAQueryFactory jpaQueryFactory,
      PostRepository postRepository) {
    this.modelMapper = modelMapper;
    this.jpaQueryFactory = jpaQueryFactory;
    this.postRepository = postRepository;
  }

  @Transactional
  public void createPost(PostDto postDto, String userId) {
    Post post = modelMapper.map(postDto, Post.class);
    User user = jpaQueryFactory.select(Projections.fields(User.class, qUser.id))  // 필요한 필드만 선택
        .from(qUser).where(qUser.loginId.eq(userId)).fetchFirst();
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
    jpaQueryFactory.update(qPost)
        .where(qPost.id.eq(postDto.getId()))
        .set(qPost.title, postDto.getTitle())
        .set(qPost.contents, postDto.getContents())
        .execute();
  }

  @Transactional
  public void deletePost(Long postId) {
    jpaQueryFactory.delete(qPost)
        .where(qPost.id.eq(postId))
        .execute();
  }

}
