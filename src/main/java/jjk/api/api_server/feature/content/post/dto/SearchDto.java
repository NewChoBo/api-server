package jjk.api.api_server.feature.content.post.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchDto {

  private String keyword;
  private String category;
  private String sort;
  private int page;
  private int size;

}
