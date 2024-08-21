package jaeseok.jaeseoklee.controller;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.student.StudentFilterDto;
import jaeseok.jaeseoklee.dto.student.StudentRegisterDto;
import jaeseok.jaeseoklee.dto.student.StudentUpdateDto;
import jaeseok.jaeseoklee.entity.student.Grade;
import jaeseok.jaeseoklee.service.StudentService;
import lombok.RequiredArgsConstructor;
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
                                      @RequestParam(name = "size", defaultValue = "20") int size){ // 한 page에 size 갯수 만큼의 학생 정보를 표기하기 위한 매개변수
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

        ResponseDto<?> result = studentService.deleteStudent(studentId/*, currentUser.getUsername()*/);

        return result;
    }

    @GetMapping("/info/filtering")
    public ResponseDto<?> studentFiltering(@RequestParam(name = "userId") String userId,
                                           @RequestParam(name = "studentGrade") Grade studentGrade,
                                           @RequestParam(name = "classNum") int classNum) {
        StudentFilterDto filterDto = new StudentFilterDto(userId, studentGrade, classNum);

        ResponseDto<?> result = studentService.filteringStudent(filterDto);

        return result;
    }

}
