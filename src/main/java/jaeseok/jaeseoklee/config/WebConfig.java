package jaeseok.jaeseoklee.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Web 설정 파일
@Configuration
public class WebConfig implements WebMvcConfigurer {

//    CORS 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS 설정을 적용
                .allowedOrigins("http://localhost:3000", "https://jsl.comon.kr", "https://jsl2.comon.kr", "http://121.139.20.242:3000") // 허용할 도메인
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 허용할 헤더
                .allowCredentials(true); // 인증 정보를 포함할지 여부

        registry.addMapping("/swagger-ui/**")
                .allowedOrigins("http://localhost:3000", "https://jsl.comon.kr")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);

        registry.addMapping("/ws/**")
                .allowedOrigins("http://localhost:3000", "https://jsl.comon.kr")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}