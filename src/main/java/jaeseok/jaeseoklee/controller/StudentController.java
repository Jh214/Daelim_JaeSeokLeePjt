package jaeseok.jaeseoklee.controller;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.student.StudentRegisterDto;
import jaeseok.jaeseoklee.dto.student.StudentUpdateDto;
import jaeseok.jaeseoklee.entity.Student;
import jaeseok.jaeseoklee.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/register")
    public ResponseDto<?> studentRegister(@RequestBody StudentRegisterDto studentRegisterDto){
        ResponseDto<?> result = studentService.registerStudent(studentRegisterDto);

        return result;
    }

    @GetMapping("/info")
    public ResponseDto<?> studentInfo(@RequestParam(name = "userId") String userId,
                                      @RequestParam(name = "page", defaultValue = "0") int page,
                                      @RequestParam(name = "size", defaultValue = "10") int size){ // 한 page에 size 갯수 만큼의 학생 정보를 표기하기 위한 매개변수
        ResponseDto<?> result = studentService.studentInfo(userId, page, size);

        return result;
    }

    @PutMapping("/update")
    public ResponseDto<?> studentUpdate(@RequestBody StudentUpdateDto studentUpdateDto){
        ResponseDto<?> result = studentService.updateStudent(studentUpdateDto);

        return result;
    }

    @DeleteMapping("/delete/{studentId}")
    public ResponseDto<?> studentDelete(@PathVariable(name = "studentId") Long studentId) {
        /*// 현재 인증된 사용자 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 현재 사용자 정보를 가져옴
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();*/

        ResponseDto<?> result = studentService.deleteStudent(studentId/*, currentUser.getUsername()*/);

        return result;
    }
}
