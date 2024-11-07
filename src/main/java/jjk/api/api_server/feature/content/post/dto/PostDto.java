package jjk.api.api_server.feature.content.post.dto;

import jjk.api.api_server.feature.user.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

  private Long id;

  private UserDto user;

  private String title;

  private String contents;
}
