package jaeseok.jaeseoklee.controller;

import jaeseok.jaeseoklee.dto.*;
import jaeseok.jaeseoklee.dto.user.ConfirmPasswordDto;
import jaeseok.jaeseoklee.dto.user.LoginDto;
import jaeseok.jaeseoklee.dto.user.SignUpDto;
import jaeseok.jaeseoklee.dto.user.UpdateDto;
import jaeseok.jaeseoklee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;
        @PostMapping("/signup")
        public ResponseDto<?> signUp(@RequestBody SignUpDto requestBody) {
            ResponseDto<?> result = userService.signUp(requestBody);
            return result;
        }

        @PostMapping("/login")
    public ResponseDto<?> login(@RequestBody LoginDto loginDto){
            ResponseDto<?> result = userService.login(loginDto);
            return result;
        }

        @PostMapping("/logout")
    public ResponseDto<?> logout(){
            ResponseDto<?> result = userService.logout();
            return result;
        }

    @PostMapping("/confirmPassword/{userId}")
    public ResponseDto<?> confirmPassowrd(@PathVariable(name = "userId") String userId, @RequestBody ConfirmPasswordDto confirmPasswordDto){
        ResponseDto<?> result = userService.confirmPw(userId, confirmPasswordDto);
        return result;
    }

        @PutMapping("/update/{userId}")
    public ResponseDto<?> update(@PathVariable(name = "userId") String userId,
                                 @RequestBody UpdateDto updateDto,
                                 @RequestHeader("PasswordVerAuth") String token){
            // Bearer 제거하고 실제 토큰 값 전달
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

            ResponseDto<?> result = userService.update(userId, updateDto, jwtToken);
            return result;
        }

        @DeleteMapping("/delete/{userId}")
    public ResponseDto<?> delete(@PathVariable(name = "userId") String userId){
            ResponseDto<?> result = userService.delete(userId);
            return result;
        }

        @GetMapping("/detail")
    public ResponseDto<?> userDetail(@RequestParam(name = "userId") String userId){
            ResponseDto<?> result = userService.userDetail(userId);
            return result;
        }

        @PostMapping("/checkId/{userId}")
    public ResponseDto<?> CheckId(@PathVariable(name = "userId") String userId){
            ResponseDto<?> result = userService.checkId(userId);

            return result;
        }

    @PostMapping("/checkEmail/{userEmail}")
    public ResponseDto<?> CheckEmail(@PathVariable(name = "userEmail") String userEmail){
        ResponseDto<?> result = userService.checkEmail(userEmail);

        return result;
    }

    @PostMapping("/checkNum/{CheckNum}")
    public ResponseDto<?> CheckNum(@PathVariable(name = "CheckNum") String CheckNum){
        ResponseDto<?> result = userService.checkNum(CheckNum);

        return result;
    }
}