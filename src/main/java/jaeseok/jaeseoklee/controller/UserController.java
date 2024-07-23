package jaeseok.jaeseoklee.controller;

import jaeseok.jaeseoklee.dto.LoginDto;
import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.SignUpDto;
import jaeseok.jaeseoklee.entity.User;
import jaeseok.jaeseoklee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
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
}
