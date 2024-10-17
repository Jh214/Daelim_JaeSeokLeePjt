package jaeseok.jaeseoklee.controller.student;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.student.StudentFilterDto;
import jaeseok.jaeseoklee.service.student.StudentAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studentForAttendance")
public class StudentAttendanceController {
    private final StudentAttendanceService studentAttendanceService;

    @GetMapping("/view")
    public ResponseDto<?> viewStudentAttendance(@RequestBody StudentFilterDto filterDto) {
        ResponseDto<?> result = studentAttendanceService.view(filterDto);

        return result;
    }
}
