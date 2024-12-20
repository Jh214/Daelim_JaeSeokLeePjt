package jaeseok.jaeseoklee.controller.student;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.student.StudentFilterDto;
import jaeseok.jaeseoklee.dto.student.StudentRegisterDto;
import jaeseok.jaeseoklee.dto.student.StudentUpdateDto;
import jaeseok.jaeseoklee.service.student.StudentService;
import jaeseok.jaeseoklee.service.student.StudentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/register")
    public ResponseDto<?> studentRegister(@RequestBody StudentRegisterDto studentRegisterDto) {
        ResponseDto<?> result = studentService.registerStudent(studentRegisterDto);

        return result;
    }

    @GetMapping("/info")
    public ResponseDto<?> studentInfo(@ModelAttribute StudentFilterDto studentFilterDto) { // 한 page에 size 갯수 만큼의 학생 정보를 표기하기 위한 매개변수
        ResponseDto<?> result = studentService.studentInfo(studentFilterDto);

        return result;
    }

    @PatchMapping("/update")
    public ResponseDto<?> studentUpdate(@RequestBody StudentUpdateDto studentUpdateDto) {
        ResponseDto<?> result = studentService.updateStudent(studentUpdateDto);

        return result;
    }

    @DeleteMapping("/delete/{studentId}")
    public ResponseDto<?> studentDelete(@PathVariable(name = "studentId") Long studentId) {

        ResponseDto<?> result = studentService.deleteStudent(studentId);

        return result;
    }

}
