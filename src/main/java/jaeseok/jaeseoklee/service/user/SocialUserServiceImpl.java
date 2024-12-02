package jaeseok.jaeseoklee.service.user;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.jwt.JwtTokenDto;
import jaeseok.jaeseoklee.entity.User;
import jaeseok.jaeseoklee.repository.UserRepository;
import jaeseok.jaeseoklee.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class SocialUserServiceImpl implements SocialUserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseDto<JwtTokenDto> processGoogleLogin(OAuth2User oAuth2User) {
        // Google에서 제공하는 사용자 정보 추출
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // 사용자 확인 또는 신규 등록
        User user = userRepository.findByUserEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .userId(email)
                            .userRealName(name)
                            .userEmail(email)
                            .userPw("GOOGLE_USER")  // Google 사용자용 임시 비밀번호
                            .roles(List.of("USER")) // 기본 역할 설정, 필요에 따라 수정 가능
                            .build();
                    return userRepository.save(newUser);
                });

        // Authentication 객체 생성
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getUserId(),
                null,
                Collections.singleton(new SimpleGrantedAuthority("USER"))
        );

        // JWT 토큰 생성
        JwtTokenDto tokenDto = jwtTokenProvider.generateToken(auth, user.getUserId());

        return ResponseDto.setSuccessData("로그인 성공", tokenDto);
    }
}