package jaeseok.jaeseoklee.service;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.student.StudentRegisterDto;
import jaeseok.jaeseoklee.dto.student.StudentUpdateDto;
import jaeseok.jaeseoklee.dto.student.StudentViewDto;
import jaeseok.jaeseoklee.entity.Student;
import jaeseok.jaeseoklee.entity.User;
import jaeseok.jaeseoklee.repository.StudentRepository;
import jaeseok.jaeseoklee.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public ResponseDto<?> registerStudent(StudentRegisterDto registerDto) {
        Optional<User> userOptional = userRepository.findById(registerDto.getUid());
        if (!userOptional.isPresent()) {
            return ResponseDto.setFailed("잘못된 요청입니다.");
        }

        User user = userOptional.get();

        Student student = Student.builder()
                .studentName(registerDto.getStudentName())
                .studentNum(registerDto.getStudentNum())
                .studentGender(registerDto.getStudentGender())
                .studentAge(registerDto.getStudentAge())
                .schoolName(registerDto.getSchoolName())
                .classNum(registerDto.getClassNum())
                .user(user) // 다대일 연관관계 설정
                .build();

        studentRepository.save(student);

        return ResponseDto.setSuccess("학생이 등록되었습니다.");
    }

    public ResponseDto<?> studentInfo(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Student> studentsPage = studentRepository.findByUserId(userId, pageable);

        if (studentsPage.isEmpty()) {
            return ResponseDto.setFailed("학생 정보가 없습니다.");
        }
        List<StudentViewDto> viewDto = studentsPage.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseDto.setSuccessData("학생 정보를 조회했습니다.", viewDto);
    }

    private StudentViewDto convertToDto(Student student) {
        return new StudentViewDto(
                student.getStudentName(),
                student.getStudentNum(),
                student.getStudentGender(),
                student.getStudentAge(),
                student.getSchoolName(),
                student.getClassNum()
        );
    }

    public ResponseDto<?> updateStudent(StudentUpdateDto updateDto) {
        // 사용자 ID로 User 객체 조회
        User user = userRepository.findById(updateDto.getUid())
                .orElseThrow(() -> new RuntimeException("잘못된 요청입니다."));

        // 학생 ID로 Student 객체 조회
        Student student = studentRepository.findById(updateDto.getStudentId())
                .orElseThrow(() -> new RuntimeException("학생 정보를 찾을 수 없습니다."));

        // 학생이 해당 유저에 속하는지 확인
        if (!student.getUser().equals(user)) {
            return ResponseDto.setFailed("해당 유저의 학생이 아닙니다.");
        }

        // 업데이트할 필드만 설정
        if (updateDto.getStudentName() != null) {
            student.setStudentName(updateDto.getStudentName());
        }
        if (updateDto.getStudentNum() != null) {
            student.setStudentNum(updateDto.getStudentNum());
        }
        if (updateDto.getStudentAge() != null) {
            student.setStudentAge(updateDto.getStudentAge());
        }

        // 업데이트된 Student 객체 저장
        try {
            studentRepository.save(student);
            return ResponseDto.setSuccess("학생 정보가 수정되었습니다.");
        } catch (Exception e) {
            return ResponseDto.setFailed("데이터베이스 연결에 실패하였습니다.");
        }
    }

    public ResponseDto<?> deleteStudent(Long studentId/*, String currentUserId*/) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);

        if (!optionalStudent.isPresent()) {
            return ResponseDto.setFailed("해당 학생을 찾을 수 없습니다.");
        }

        try {
            studentRepository.deleteById(studentId);
        } catch (Exception e) {
            return ResponseDto.setFailed("데이터베이스 연결에 실패하였습니다.");
        }

        return ResponseDto.setSuccess("학생이 성공적으로 삭제되었습니다.");
    }

}
