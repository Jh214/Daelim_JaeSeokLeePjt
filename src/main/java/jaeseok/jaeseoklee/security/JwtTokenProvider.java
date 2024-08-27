package jaeseok.jaeseoklee.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jaeseok.jaeseoklee.dto.jwt.JWTConfirmPasswordTokenDto;
import jaeseok.jaeseoklee.dto.jwt.JwtTokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;
    private final Duration expiration;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.expiration}") Duration expiration) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expiration = expiration;
    }

    // User 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
    public JwtTokenDto generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + expiration.toMillis()); // 토큰 만료시간 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName()) // 인증된 사용자로 토큰의 대상 지정
                .claim("auth", authorities) // 클레임 설정
                .setExpiration(accessTokenExpiresIn) // 토큰 만료시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // secretKey 와 HS256알고리즘으로 토큰 서명
                .compact(); // accessToken 설정을 마치고 JWT 토큰을 문자열로 반환

        // Refresh Token 생성 (위에서 발급한 accessToken 의 만료시간을 연장 또는 재생 하기 위한 토큰)
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + expiration.toMillis())) // 만료시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // 토큰 서명
                .compact(); // 토큰 반환

        return JwtTokenDto.builder()
                .grantType("Bearer") // 토큰 타입을 "Bearer"로 설정
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

//    비밀번호 검증을 위한 토큰 생성 메서드
    public JWTConfirmPasswordTokenDto generatePasswordVerificationToken(Authentication authentication) {
        // 인증된 사용자의 정보 가져오기
        String username = authentication.getName();

        // 현재 시간 가져오기
        long now = (new Date()).getTime();

        // 비밀번호 검증 토큰의 만료 시간을 설정 (예: 10분)
        long expirationInMillis = Duration.ofMinutes(10).toMillis();
        Date tokenExpirationDate = new Date(now + expirationInMillis);

        // 비밀번호 검증 토큰 생성
        String passwordVerificationToken = Jwts.builder()
                .setSubject(username)
                .claim("purpose", "password_verification") // purpose 클레임이 password_verification 을 포함함
                .setExpiration(tokenExpirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact(); //

        return JWTConfirmPasswordTokenDto.builder()
                .grantType("Bearer")
                .accessPwToken(passwordVerificationToken)
                .build();
    }


    // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // Jwt 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication return
        // UserDetails: interface, User: UserDetails를 구현한 class
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public Authentication getPwAuthentication(String accessPwToken) {
        Claims claims = parseClaims(accessPwToken);

        // "purpose" 클레임이 없으며 password_verification 라는 값을 가진 purpose 클레임이 아니면 권한이 없다는 예외처리
        if (claims.get("purpose") == null || !"password_verification".equals(claims.get("purpose"))) {
            throw new RuntimeException("비밀번호 검증 권한 정보가 없는 토큰입니다.");
        }

        // 비밀번호 검증 토큰은 권한 정보가 필요 없음
        Collection<? extends GrantedAuthority> authorities = Collections.emptyList();

        // UserDetails 객체를 생성하여 Authentication 반환
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }


    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key) // 서명을 검증할 secretKey 를 가져옴
                    .build()
                    .parseClaimsJws(token); // 서명을 검증하고 클레임 추출
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    public boolean validatePasswordVerificationToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return "password_verification".equals(claims.get("purpose")); // purpose 클레임이 password_verification 을 가지고 있는지 검증
        } catch (Exception e) {
            log.info("Invalid Password Verification Token", e);
        }
        return false;
    }


    // accessToken 파싱하는 메서드
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
