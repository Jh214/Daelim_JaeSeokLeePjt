package jaeseok.jaeseoklee.controller;

import jaeseok.jaeseoklee.dto.*;
import jaeseok.jaeseoklee.dto.user.*;
import jaeseok.jaeseoklee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

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
    @PostMapping("/confirmPassword/{userId}")
    public ResponseDto<?> confirmPassowrd(@PathVariable(name = "userId") String userId, @RequestBody ConfirmPasswordDto confirmPasswordDto) {
        ResponseDto<?> result = userService.confirmPw(userId, confirmPasswordDto);
        return result;
    }

//    개인정보 변경
    @PutMapping("/update/{userId}")
    public ResponseDto<?> update(@PathVariable(name = "userId") String userId,
                                 @RequestBody UpdateDto updateDto,
                                 @RequestHeader("PasswordVerAuth") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        ResponseDto<?> result = userService.update(userId, updateDto, jwtToken);
        return result;
    }

//    비밀번호 변경
    @PutMapping("/updatePassword/{userId}")
    public ResponseDto<?> updatePassword(@PathVariable(name = "userId") String userId,
                                         @RequestBody UpdatePasswrodDto updatePasswordDto,
                                         @RequestHeader("PasswordVerAuth") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        ResponseDto<?> result = userService.updatePassword(userId, updatePasswordDto, jwtToken);
        return result;
    }

//    회원탈퇴
    @DeleteMapping("/delete/{userId}")
    public ResponseDto<?> delete(@PathVariable(name = "userId") String userId,
                                 @RequestHeader("PasswordVerAuth") String token) {

        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        ResponseDto<?> result = userService.delete(userId, jwtToken);
        return result;
    }

//    개인정보 변경 페이지
    @GetMapping("/detail")
    public ResponseDto<?> userDetail(@RequestParam(name = "userId") String userId) {
        ResponseDto<?> result = userService.userDetail(userId);
        return result;
    }

//    아이디 중복 검사
    @PostMapping("/checkId/{userId}")
    public ResponseDto<?> CheckId(@PathVariable(name = "userId") String userId) {
        ResponseDto<?> result = userService.checkId(userId);

        return result;
    }

//    이메일 중복 검사
    @PostMapping("/checkEmail/{userEmail}")
    public ResponseDto<?> CheckEmail(@PathVariable(name = "userEmail") String userEmail) {
        ResponseDto<?> result = userService.checkEmail(userEmail);

        return result;
    }

//    전화번호 중복검사
    @PostMapping("/checkNum/{CheckNum}")
    public ResponseDto<?> CheckNum(@PathVariable(name = "CheckNum") String CheckNum) {
        ResponseDto<?> result = userService.checkNum(CheckNum);

        return result;
    }

//    인증메일 보내기
    @PostMapping("/sendEmail")
    public ResponseDto<?> sendEmail(@RequestBody SendEmailDto mailDto) {
        ResponseDto<?> result = userService.sendFindPasswordEmailCode(mailDto);

        return result;
    }

//    이메일 인증 확인
    @PostMapping("/verificationEmailCode")
    public ResponseDto<?> verCode(@RequestBody VerificationCodeDto verCodeDto){

        ResponseDto<?> result = userService.verificationEmailCode(verCodeDto);

        return result;
    }

//    비밀번호 찾기를 통해 이메일 인증이 완료된 사용자에 한하여 비밀번호 변경
    @PutMapping("/findPassword")
    public ResponseDto<?> updatePassword(@RequestBody FindPasswordDto findPasswordDto,
                                         @RequestHeader("EmailVerAuth") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        ResponseDto<?> result = userService.findPassword(findPasswordDto, jwtToken);
        return result;
    }
}