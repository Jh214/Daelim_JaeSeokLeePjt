package jaeseok.jaeseoklee.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//swagger 설정파일
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version("v1.0.0")
                .title("JSL 테스트 서버")
                .description("API Description");

        Server server = new Server();
        server.setUrl("https://jsl.comon.kr");

        return new OpenAPI()
                .info(info);
    }
}
