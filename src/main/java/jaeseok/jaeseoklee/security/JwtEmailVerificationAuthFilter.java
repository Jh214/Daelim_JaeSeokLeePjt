package jaeseok.jaeseoklee.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jaeseok.jaeseoklee.dto.user.find.FindPasswordDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtEmailVerificationAuthFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String token = resolveToken(request);

        // /api/user/findPassword 엔드포인트에서만 검증
        if (requestURI.startsWith("/api/user/findPassword")) {
            if (token != null) {
                // 리퀘스트 바디에서 userId 읽기
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                try (BufferedReader reader = request.getReader()) {
                    while ((line = reader.readLine()) != null) {
                        jsonBuilder.append(line);
                    }
                }
                // JSON 문자열을 객체로 변환
                String requestBody = jsonBuilder.toString();
                JSONObject jsonObject = new JSONObject(requestBody); // org.json.JSONObject 사용
                String jsonUserId = jsonObject.getString("userId"); // userId 추출

                // 토큰 검증
                if (jwtTokenProvider.validateEmailVerificationToken(token, jsonUserId)) {
                    Authentication authentication = jwtTokenProvider.getEmailAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("EmailVerAuth"); // 헤더 key "EmailVerAuth" 이어야 함
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
