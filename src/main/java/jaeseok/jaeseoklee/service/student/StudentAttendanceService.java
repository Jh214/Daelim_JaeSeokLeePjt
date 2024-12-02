package jaeseok.jaeseoklee.service.student;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.student.StudentFilterDto;


public interface StudentAttendanceService {

    ResponseDto<?> view(StudentFilterDto filterDto);
}
