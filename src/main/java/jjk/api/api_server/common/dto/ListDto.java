package jjk.api.api_server.common.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListDto<T> {

  private List<T> items;
  private Long totalCount;
}
