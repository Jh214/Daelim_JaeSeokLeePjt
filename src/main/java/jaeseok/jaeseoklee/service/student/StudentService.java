package jaeseok.jaeseoklee.service.student;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.student.StudentFilterDto;
import jaeseok.jaeseoklee.dto.student.StudentRegisterDto;
import jaeseok.jaeseoklee.dto.student.StudentUpdateDto;
import jakarta.validation.Valid;


public interface StudentService {

    ResponseDto<?> registerStudent(StudentRegisterDto registerDto);

    ResponseDto<?> studentInfo(StudentFilterDto studentFilterDto);

    ResponseDto<?> updateStudent(@Valid StudentUpdateDto updateDto);

    ResponseDto<?> deleteStudent(Long studentId);


}
