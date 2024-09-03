package jaeseok.jaeseoklee.controller.user;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.user.find.FindPasswordDto;
import jaeseok.jaeseoklee.dto.user.find.FindUserIdSendEmailDto;
import jaeseok.jaeseoklee.dto.user.find.SendEmailDto;
import jaeseok.jaeseoklee.dto.user.find.VerificationCodeDto;
import jaeseok.jaeseoklee.service.user.FindUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class FindUserController {
    @Autowired
    FindUserService findUserService;

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
    @PutMapping("/findPassword")
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
}
