package jaeseok.jaeseoklee.service.student;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.student.StudentFilterDto;
import jaeseok.jaeseoklee.dto.student.StudentViewDto;
import jaeseok.jaeseoklee.dto.student.attendance.AttendanceViewDto;
import jaeseok.jaeseoklee.entity.User;
import jaeseok.jaeseoklee.entity.student.Attendance;
import jaeseok.jaeseoklee.entity.student.Student;
import jaeseok.jaeseoklee.entity.student.StudentAttendanceTable;
import jaeseok.jaeseoklee.repository.UserRepository;
import jaeseok.jaeseoklee.repository.student.StudentAttendanceTableRepository;
import jaeseok.jaeseoklee.repository.student.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StudentAttendanceService {
    private final StudentRepository studentRepository;
    private final StudentAttendanceTableRepository studentAttendanceTableRepository;
    private final UserRepository userRepository;

    public ResponseDto<?> view(StudentFilterDto filterDto){
        String userId = filterDto.getUserId();
        int page = filterDto.getPage();
        int size = filterDto.getSize();
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        log.info(userId);

        if (!optionalUser.isPresent()) {
            return ResponseDto.setFailed("잘못된 요청입니다.");
        }
        Pageable pageable = PageRequest.of(page, size);

        // 필터링된 학생 목록 조회
        Page<Student> studentsPage = studentRepository.findStudentsByUserIdAndGrade(filterDto, pageable);

        if (studentsPage.isEmpty()) {
            return ResponseDto.setFailed("조건에 맞는 학생 정보가 없습니다.");
        }

        List<Student> students = studentsPage.getContent();

        List<AttendanceViewDto> studentViewDto = students.stream()
                .map(this::convertToDto)  // `convertToDto` 메서드를 통해 변환
                .collect(Collectors.toList());

        return ResponseDto.setSuccessData("학생 정보를 불러왔습니다.", studentViewDto);
    }

    private AttendanceViewDto convertToDto(Student student) {
        StudentAttendanceTable studentAttendanceTable = (StudentAttendanceTable) student.getStudentAttendanceTable();

        return new AttendanceViewDto(
                student.getStudentName(),
                student.getStudentGrade(),
                student.getClassNum(),
                studentAttendanceTable.getAttendanceStatus(),
                studentAttendanceTable.getAttendanceReason()
        );
    }
}
