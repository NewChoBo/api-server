package jjk.api.api_server.feature.user.auth.dto;

import lombok.Data;

@Data
public class SignUpDto {

    private String username;
    private String loginId;
    private String password;

}
