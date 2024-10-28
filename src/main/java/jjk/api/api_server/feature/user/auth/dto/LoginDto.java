package jjk.api.api_server.feature.user.auth.dto;

import lombok.Data;

@Data
public class LoginDto {

  private String username;
  private String password;

}
