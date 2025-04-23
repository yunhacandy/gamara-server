package gamara.server.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Gamara API 문서") // 문서 제목
                .description("가? 마라? 프로젝트의 REST API 명세입니다.\n리뷰, 추천, 통계, 사용자 관련 기능을 포함합니다.") // 설명
                .version("v1.0.0")
                .contact(new Contact()
                        .name("Gamara 팀")
                        .email("yunhacandy@gmail.com")
                        .url("https://github.com/yunhacandy/gamara-server"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }
}
