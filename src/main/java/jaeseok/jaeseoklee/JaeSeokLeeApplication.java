package jaeseok.jaeseoklee;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@OpenAPIDefinition(servers = {@Server(url = "https://jsl.comon.kr/", description = "jsl 도메인 허용")})
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class JaeSeokLeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(JaeSeokLeeApplication.class, args);
    }

}
