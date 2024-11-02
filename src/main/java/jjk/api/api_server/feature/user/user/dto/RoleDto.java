package jjk.api.api_server.feature.user.user.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

  private Long id;

  private String name;

  private List<UserDto> users;

  private String memo;

}
