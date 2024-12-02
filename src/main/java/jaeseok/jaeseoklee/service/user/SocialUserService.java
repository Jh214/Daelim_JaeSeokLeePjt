package jaeseok.jaeseoklee.service.user;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.jwt.JwtTokenDto;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface SocialUserService {

    ResponseDto<JwtTokenDto> processGoogleLogin(OAuth2User oAuth2User);
}
