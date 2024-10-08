package jaeseok.jaeseoklee.security;

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

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtPasswordVerificationAuthFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String token = resolveToken(request);

        // 요청에서 userId 추출 (예: URL의 쿼리 파라미터에서 가져올 수 있음)
        String expectedUserId = request.getParameter("userId"); // userId가 쿼리 파라미터로 전달된다고 가정

        // /api/user/updatePassword/, /api/user/update/, /api/user/delete/ 엔드포인트에서만 검증
        if (requestURI.startsWith("/api/user/updatePassword/") ||
                requestURI.startsWith("/api/user/update/") ||
                requestURI.startsWith("/api/user/delete/")) {

            if (token != null && expectedUserId != null &&
                    jwtTokenProvider.validatePasswordVerificationToken(token, expectedUserId)) {
                Authentication authentication = jwtTokenProvider.getPwAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
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
