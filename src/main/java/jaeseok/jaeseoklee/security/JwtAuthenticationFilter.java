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

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // 1. Request Header 에서 JWT 토큰 추출
        String token = resolveToken(request);
        String requestURI = request.getRequestURI();
        BufferedHttpServletRequestWrapper wrappedRequest = new BufferedHttpServletRequestWrapper(request);


        // /api/user/findPassword/, /api/user/signup/ 엔드포인트에서는 검증 안함
        if (!requestURI.startsWith("/api/user/findPassword/") && !requestURI.startsWith("/api/user/checkNum")) {
            log.info("requestURI = " + requestURI);
            // 2. validateToken 으로 토큰 유효성 검사
            Claims claims = jwtTokenProvider.validateToken(token);
            if (claims != null) {
                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                StringBuilder jsonBuilder = new StringBuilder();
                String line;

                // BufferedHttpServletRequestWrapper를 통해 본문을 읽음
                try (BufferedReader reader = wrappedRequest.getReader()) {
                    while ((line = reader.readLine()) != null) {
                        jsonBuilder.append(line);
                    }
                }

                String requestBody = jsonBuilder.toString();

                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, String> requestData = objectMapper.readValue(requestBody, new TypeReference<Map<String, String>>() {
                    });
                    log.info("request Data = " + requestData.toString());

                    String jsonUserId = requestData.get("userId"); // JSON에서 userId 추출
                    log.info("jsonUserId = " + jsonUserId);


                // 요청 본문이 비어 있을 때만 쿼리 파라미터에서 userId를 가져옴
//                if (requestBody.isEmpty()) {
                    String requestUserId = request.getParameter("userId");
                    log.info("paramUserId = " + requestUserId);
//                }

                    // tokenUserId는 항상 추출
                    String tokenUserId = claims.getSubject(); // 토큰에서 userId 가져오기

                    // requestUserId가 null이 아닐 때만 검증
                    if (requestUserId != null && !requestUserId.equals(tokenUserId)) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden 상태 코드 설정
                        response.setContentType("application/json; charset=UTF-8"); // 응답 형식 JSON
                        response.getWriter().write("{\"error\":\"권한이 없는 사용자입니다.\"}"); // JSON 형식으로 응답 내용 작성
                        return; // 필터 체인의 다음 단계로 진행하지 않도록 return
                    }

                    // jsonUserId가 null이 아닐 때만 검증
                    if (jsonUserId != null && !jsonUserId.equals(tokenUserId)) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden 상태 코드 설정
                        response.setContentType("application/json; charset=UTF-8"); // 응답 형식 JSON
                        response.getWriter().write("{\"error\":\"권한이 없는 사용자입니다.\"}"); // JSON 형식으로 응답 내용 작성
                        return; // 필터 체인의 다음 단계로 진행하지 않도록 return
                    }
                }
            }
            filterChain.doFilter(wrappedRequest, response);
        }



    // Request Header 에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization"); // 헤더 key "Authorization"
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}