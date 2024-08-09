package jaeseok.jaeseoklee.controller;

import jaeseok.jaeseoklee.dto.*;
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

        @PutMapping("/update/{userId}")
    public ResponseDto<?> update(@PathVariable(name = "userId") String userId, @RequestBody UpdateDto updateDto){
            ResponseDto<?> result = userService.update(userId, updateDto);
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
}