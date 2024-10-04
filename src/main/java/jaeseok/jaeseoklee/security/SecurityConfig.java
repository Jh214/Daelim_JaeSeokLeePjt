package jaeseok.jaeseoklee.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String[] AUTH_WHITE_LIST = {
            "/api/user/signup",
            "/api/user/login",
            "/api/user/checkId/**",
            "/api/user/checkEmail/**",
            "/api/user/checkNum/**",
            "/api/user/sendEmail",
            "/api/user/verificationEmailCode",
            "/api/user/verificationSignUpEmailCode",
            "/api/user/findUserIdByUserEmailCode",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/ws/**",
            "/app/**",
            "/topic/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                                .requestMatchers(AUTH_WHITE_LIST).permitAll() // 해당 엔드포인트는 접근 허용
////                        .requestMatchers("/api/user/**", "/api/student/**").hasRole("USER") <- 이건 자동으로 문자열 앞에 "ROLE_" 이 추가됨
//                        .requestMatchers("/api/student/**", "/api/user/**").hasAuthority("USER") // "USER" 역할(role)을 가진 사용자만 허용
                        .requestMatchers("/api/user/**").authenticated() // 토큰 검증이 완료된 사용자만 허용
//                                .requestMatchers("/api/user/updatePassword/**", "/api/user/update/**").authenticated() // 임시로 얘네만 검증 걸어둠
                                .anyRequest().permitAll() // 나머지 모든 엔드포인트 허용
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(customAuthenticationEntryPoint())  // 인증이 필요한 경우
                                .accessDeniedHandler(accessDeniedHandler()) // 권한이 없는 경우
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtPasswordVerificationAuthFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtEmailVerificationAuthFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value()); // 403에러 코드 설정
            response.setContentType("application/json;charset=UTF-8"); // 컨텐츠 타입과 인코딩 설정

            String jsonResponse = "{\"message\": \"권한이 없습니다.\"}"; // JSON 문자열 데이터

            response.getWriter().write(jsonResponse); // JSON 문자열로 반환
        };
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401에러 코드 설정
            response.setContentType("application/json;charset=UTF-8");

            String jsonResponse = "{\"message\": \"인증이 필요합니다.\"}";

            response.getWriter().write(jsonResponse);
        };
    }


}