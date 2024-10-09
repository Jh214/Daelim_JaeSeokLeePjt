package jaeseok.jaeseoklee.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtPasswordVerificationAuthFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String token = resolveToken(request);
        BufferedHttpServletRequestWrapper wrappedRequest = new BufferedHttpServletRequestWrapper(request);

        // 요청에서 userId 추출 (예: URL의 쿼리 파라미터에서 가져올 수 있음)
        String expectedUserId = request.getParameter("userId"); // userId가 쿼리 파라미터로 전달된다고 가정

        // /api/user/updatePassword/, /api/user/update/, /api/user/delete/ 엔드포인트에서만 검증
        if (requestURI.startsWith("/api/user/updatePassword/") ||
                requestURI.startsWith("/api/user/update/") ||
                requestURI.startsWith("/api/user/delete/")) {
            Claims claims = jwtTokenProvider.validateToken(token);
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            // BufferedHttpServletRequestWrapper를 통해 본문을 읽음
            try (BufferedReader reader = wrappedRequest.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
            }

            String requestBody = jsonBuilder.toString();
            String jsonUserId = null;

            // 요청 본문이 비어 있지 않은 경우에만 JSON 검증 수행
            if (!requestBody.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> requestData = objectMapper.readValue(requestBody, new TypeReference<Map<String, String>>() {});
                log.info("request Data = " + requestData.toString());

                jsonUserId = requestData.get("userId"); // JSON에서 userId 추출
                log.info("jsonUserId = " + jsonUserId);
            }
            String tokenUserId = claims.getSubject();

            if (token != null && expectedUserId != null &&
                    jwtTokenProvider.validatePasswordVerificationToken(token, expectedUserId)) {
                Authentication authentication = jwtTokenProvider.getPwAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // jsonUserId가 null이 아닐 때만 검증
            if (jsonUserId != null && !jsonUserId.equals(tokenUserId)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden 상태 코드 설정
                response.setContentType("application/json; charset=UTF-8"); // 응답 형식 JSON
                response.getWriter().write("{\"error\":\"권한이 없는 사용자입니다.\"}"); // JSON 형식으로 응답 내용 작성
                return; // 필터 체인의 다음 단계로 진행하지 않도록 return
            }
        }

        filterChain.doFilter(request, response);
    }


    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("PasswordVerAuth"); // 헤더 key "PasswordVerAuth" 이어야 함
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
