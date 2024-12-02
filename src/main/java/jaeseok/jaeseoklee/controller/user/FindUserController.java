package jaeseok.jaeseoklee.controller.user;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.user.find.*;
import jaeseok.jaeseoklee.service.user.FindUserService;
import jaeseok.jaeseoklee.service.user.FindUserServiceImpl;
import jaeseok.jaeseoklee.service.user.SMS_KAKAO_Service;
import jaeseok.jaeseoklee.service.user.SMS_KAKAO_ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class FindUserController {

    private final FindUserService findUserService;
    private final SMS_KAKAO_Service smsKakaoService;

    //    인증메일 보내기
    @PostMapping("/sendEmail")
    public ResponseDto<?> sendEmail(@RequestBody SendEmailDto mailDto) {
        ResponseDto<?> result = findUserService.sendFindPasswordEmailCode(mailDto);

        return result;
    }

    //    이메일 인증 확인
    @PostMapping("/verificationEmailCode")
    public ResponseDto<?> verCode(@RequestBody VerificationCodeDto verCodeDto){

        ResponseDto<?> result = findUserService.verificationEmailCode(verCodeDto);

        return result;
    }

    //    비밀번호 찾기를 통해 이메일 인증이 완료된 사용자에 한하여 비밀번호 변경
    @PatchMapping("/findPassword")
    public ResponseDto<?> updatePassword(@RequestBody FindPasswordDto findPasswordDto,
                                         @RequestHeader("EmailVerAuth") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        ResponseDto<?> result = findUserService.findPassword(findPasswordDto, jwtToken);
        return result;
    }

    @PostMapping("/findUserIdByUserEmailCode")
    public ResponseDto<?> findUserIdByUserEmailCode(@RequestBody FindUserIdSendEmailDto findUserIdSendEmailDto) {
        ResponseDto<?> result = findUserService.sendFindUserIdEmailCode(findUserIdSendEmailDto);

        return result;
    }

    @PostMapping("/findUserIdBySmsCode")
    public ResponseDto<?> findUserIdBySmsCode(@RequestBody FindUserIdBySmsCodeSend smsCodeSend) {
        ResponseDto<?> result = smsKakaoService.FindUserIdSendKakao(smsCodeSend);

        return result;
    }

    @PostMapping("/sendKakao")
    public ResponseDto<?> sendEmail(@RequestBody FindUserPasswordByUserNum sendKakao) {
        ResponseDto<?> result = smsKakaoService.sendKakao(sendKakao);

        return result;
    }

    @PostMapping("/userPwVerificationSmsCode")
    public ResponseDto<?> userPwVerSmsCode(@RequestBody VerificationPwSmsCode verSmsCode) {
        ResponseDto<?> result = smsKakaoService.findPwVerificationKakaoCode(verSmsCode);

        return result;
    }
}
