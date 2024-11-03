package jjk.api.api_server.feature.content.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jjk.api.api_server.common.dto.ListDto;
import jjk.api.api_server.feature.content.post.dto.PostDto;
import jjk.api.api_server.feature.content.post.dto.SearchDto;
import jjk.api.api_server.feature.content.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/post")
@Tag(name = "Posts", description = "게시글 기능")
public class PostController {

  private final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  @PostMapping
  @Operation(summary = "Create Post", description = "Post 등록")
  public ResponseEntity<String> createPost(@RequestBody PostDto postDto) {
    // JWT 토큰에서 인증된 사용자 정보 가져오기
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = null;

    if (authentication.getPrincipal() != null) {
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      userId = userDetails.getUsername();  // UserDetails에 저장된 ID 가져오기
    }

    postService.createPost(postDto, userId);
    return new ResponseEntity<>("삽입 성공", HttpStatus.OK);
  }

  @GetMapping("/list")
  @Operation(summary = "Get All Posts", description = "모든 Post 조회")
  public ResponseEntity<ListDto<PostDto>> getPostList(SearchDto searchDto) {
    ListDto<PostDto> postDtoList = postService.getPostList(searchDto);
    return new ResponseEntity<>(postDtoList, HttpStatus.OK);
  }

  @GetMapping("/{postId}")
  @Operation(summary = "Get Post", description = "Post 조회")
  public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
    PostDto post = postService.getPost(postId);
    return new ResponseEntity<>(post, HttpStatus.OK);
  }

  @PutMapping
  @Operation(summary = "Update Post", description = "Post 수정")
  public ResponseEntity<String> updatePost(@RequestBody PostDto postDto) {
    return new ResponseEntity<>("수정 성공", HttpStatus.OK);
  }

  @DeleteMapping
  @Operation(summary = "Delete Post", description = "Post 삭제")
  public ResponseEntity<String> deletePost(@RequestBody PostDto postDto) {
    return new ResponseEntity<>("삭제 성공", HttpStatus.OK);
  }
}
