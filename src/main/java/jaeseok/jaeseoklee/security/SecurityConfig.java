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
            "/api/user/login",
            "/api/user/signup",
            "/api/user/checkId",
            "/api/user/checkEmail/**",
            "/api/user/checkNum",
            "/api/user/verificationSignUpSmsCode",
            "/api/user/sendEmail",
            "/api/user/verificationEmailCode",
            "/api/user/verificationSignUpEmailCode",
            "/api/user/findUserIdByUserEmailCode",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/app/**",
            "/api/user/findUserIdBySmsCode",
            "/api/user/sendKakao",
            "/api/user/userPwVerificationSmsCode",
            "/api/user/social/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(AUTH_WHITE_LIST).permitAll() // 해당 엔드포인트는 접근 허용
                        .requestMatchers("/api/user/**", "/api/student/**").authenticated() // 토큰 검증이 완료된 사용자만 허용
                        .anyRequest().permitAll() // 나머지 모든 엔드포인트 허용
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/api/user/social/google", true) // 인증 성공 후 강제 리디렉션
                        .failureUrl("/login?error=true") // 실패 시 리디렉션 경로
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
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json;charset=UTF-8");
            String jsonResponse = "{\"message\": \"권한이 없습니다.\"}";
            response.getWriter().write(jsonResponse);
        };
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            String jsonResponse = "{\"message\": \"인증이 필요합니다.\"}";
            response.getWriter().write(jsonResponse);
        };
    }
}