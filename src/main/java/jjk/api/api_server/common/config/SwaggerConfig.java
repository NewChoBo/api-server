package jjk.api.api_server.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI().info(
            new Info().title("New Chobo's API Server").version("1.0")
                .description("범용적인 프로젝트 작성을 위한 백엔드 뼈대 프로젝트입니다."))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth")).components(
            new io.swagger.v3.oas.models.Components().addSecuritySchemes("bearerAuth",
                new SecurityScheme().type(Type.HTTP).scheme("bearer").bearerFormat("JWT")
                    .in(In.HEADER).name("Authorization")));
  }
}
