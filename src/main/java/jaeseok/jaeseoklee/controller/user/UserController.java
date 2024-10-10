package jaeseok.jaeseoklee.controller.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import jaeseok.jaeseoklee.dto.*;
import jaeseok.jaeseoklee.dto.user.*;
import jaeseok.jaeseoklee.dto.user.find.VerificationCodeDto;
import jaeseok.jaeseoklee.dto.user.sms.SendKakao;
import jaeseok.jaeseoklee.dto.user.sms.VerificationSmsCode;
import jaeseok.jaeseoklee.service.user.SMS_KAKAO_Service;
import jaeseok.jaeseoklee.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원(교사)", description = "회원 관련 api")
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    SMS_KAKAO_Service smsKakaoService;

//    회원가입
    @PostMapping("/signup")
    public ResponseDto<?> signUp(@RequestBody SignUpDto requestBody) {
        ResponseDto<?> result = userService.signUp(requestBody);
        return result;
    }

//    로그인
    @PostMapping("/login")
    public ResponseDto<?> login(@RequestBody LoginDto loginDto) {
        ResponseDto<?> result = userService.login(loginDto);
        return result;
    }

//    로그아웃
    @PostMapping("/logout")
    public ResponseDto<?> logout() {
        ResponseDto<?> result = userService.logout();
        return result;
    }

//    비밀번호 인증
    @PostMapping("/confirmPassword")
    public ResponseDto<?> confirmPassowrd(@RequestBody ConfirmPasswordDto confirmPasswordDto) {
        ResponseDto<?> result = userService.confirmPw(confirmPasswordDto);
        return result;
    }

//    개인정보 변경
    @PatchMapping("/update")
    public ResponseDto<?> update(@RequestParam(name = "userId") String userId,
                                 @RequestBody UpdateDto updateDto,
                                 @RequestHeader("PasswordVerAuth") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        ResponseDto<?> result = userService.update(userId, updateDto, jwtToken);
        return result;
    }

//    비밀번호 변경
    @PatchMapping("/updatePassword")
    public ResponseDto<?> updatePassword(@RequestParam(name = "userId") String userId,
                                         @RequestBody UpdatePasswrodDto updatePasswordDto,
                                         @RequestHeader("PasswordVerAuth") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        ResponseDto<?> result = userService.updatePassword(userId, updatePasswordDto, jwtToken);
        return result;
    }

//    회원탈퇴
    @DeleteMapping("/delete")
    public ResponseDto<?> delete(@RequestParam(name = "userId") String userId,
                                 @RequestHeader("PasswordVerAuth") String token,
                                 @RequestBody UserPwDto userPwDto) {

        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        ResponseDto<?> result = userService.delete(userId, jwtToken, userPwDto);
        return result;
    }

//    개인정보 변경 페이지
    @GetMapping("/detail")
    public ResponseDto<?> userDetail(@RequestParam(name = "userId") String userId) {
        ResponseDto<?> result = userService.userDetail(userId);
        return result;
    }

//    아이디 중복 검사
    @PostMapping("/checkId")
    public ResponseDto<?> CheckId(@RequestBody UserIdDto userIdDto) {
        ResponseDto<?> result = userService.checkId(userIdDto);

        return result;
    }

//    이메일 중복 검사
    @PostMapping("/checkEmail")
    public ResponseDto<?> CheckEmail(@RequestBody UserEmailDto mailDto) {
        ResponseDto<?> result = userService.checkEmail(mailDto);

        return result;
    }

//    전화번호 중복검사
    @PostMapping("/checkNum")
    public ResponseDto<?> CheckNum(@RequestBody SendKakao sendKakao) {
        ResponseDto<?> result = smsKakaoService.validateAndSendKakao(sendKakao);

        return result;
    }
    @PostMapping("/verificationSignUpSmsCode")
    public ResponseDto<?> verSignUpSmsCode(@RequestBody VerificationSmsCode verCodeDto) {
        ResponseDto<?> result = smsKakaoService.verificationKakaoCode(verCodeDto);

        return result;
    }
}