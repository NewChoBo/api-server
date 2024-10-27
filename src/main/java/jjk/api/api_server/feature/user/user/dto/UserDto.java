package jjk.api.api_server.feature.user.user.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private Long id;
  private String username;
  private String loginId;
  private String password;
  private String email;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;

}
