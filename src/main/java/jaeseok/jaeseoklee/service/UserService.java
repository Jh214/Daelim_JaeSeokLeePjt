package jaeseok.jaeseoklee.service;

import jaeseok.jaeseoklee.dto.*;
import jaeseok.jaeseoklee.dto.user.LoginDto;
import jaeseok.jaeseoklee.dto.user.SignUpDto;
import jaeseok.jaeseoklee.dto.user.UpdateDto;
import jaeseok.jaeseoklee.dto.user.UserDetailDto;
import jaeseok.jaeseoklee.entity.User;
import jaeseok.jaeseoklee.repository.ScheduleRepository;
import jaeseok.jaeseoklee.repository.StudentRepository;
import jaeseok.jaeseoklee.repository.UserRepository;
import jaeseok.jaeseoklee.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final StudentRepository studentRepository;
    private final ScheduleRepository scheduleRepository;


    // ResponseDto를 반환하는 signUp(회원가입) 메서드
    public ResponseDto<?> signUp(SignUpDto dto) {
        String userId = dto.getUserId();
        String password = dto.getUserPw();
        String confirmPassword = dto.getUserConPw();


        // 비밀번호 확인
        if (!password.equals(confirmPassword)) {
            return ResponseDto.setFailed("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 해싱
        String hashedPassword = bCryptPasswordEncoder.encode(password);

        // UserEntity 생성
        User user = User.builder()
                .userId(userId)
                .userPw(hashedPassword) // 해시된 비밀번호
                .userName(dto.getUserName())
                .userNum(dto.getUserNum())
                .userDate(dto.getUserDate())
                .userJoin(LocalDateTime.now()) // 현재 시간으로 설정
                .userEmail(dto.getUserEmail())
                .schoolName(dto.getSchoolName())
                .classNum(dto.getClassNum())
                .roles(List.of("USER")) // 기본 역할 설정, 필요에 따라 수정 가능
                .build();

        try {
            // db에 사용자 저장
            userRepository.save(user);
        } catch (Exception e) {
            return ResponseDto.setFailed("데이터베이스 연결에 실패하였습니다.");
        }

        return ResponseDto.setSuccess("회원 생성에 성공했습니다.");
    }

    public ResponseDto<?> login(LoginDto dto) {
        String userId = dto.getUserId();
        String password = dto.getUserPw();

//        UserId 조회
        Optional<User> findUser = userRepository.findByUserId(userId);

        if (findUser.isPresent() && bCryptPasswordEncoder.matches(password, findUser.get().getUserPw())) {
//            로그인 성공
            Authentication authentication = new UsernamePasswordAuthenticationToken( // Authentication 객체 생성
                    userId,
                    null,
                    findUser.get().getAuthorities()); // 사용자의 id와 비밀번호를 통해 권한정보를 가져옴
            JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(authentication); // JWT 토큰 생성
            return ResponseDto.setSuccessData("로그인 성공", jwtTokenDto); // JSON형태로 성공메세지 + JWT 토큰 반환
        } else {
//            로그인 실패
            return ResponseDto.setFailed("아이디 또는 비밀번호를 확인해주세요.");
        }
    }

    public ResponseDto<?> logout() {
        // 클라이언트측에서 removeItem으로 토큰 제거 해주세용
        return ResponseDto.setSuccess("로그아웃 성공");
    }

    public ResponseDto<?> update(String userId, UpdateDto dto) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (!userOptional.isPresent()) {
            return ResponseDto.setFailed("해당 회원을 찾을 수 없습니다.");
        }

//        옵셔널로 찾은 userId 에 해당하는 User 정보로 UserEntity 생성
        User user = userOptional.get();

        // 비밀번호 확인
//        if (dto.getUserPw() != null && !bCryptPasswordEncoder.matches(dto.getUserPw(), user.getPassword())) {
//            return ResponseDto.setFailed("현재 비밀번호가 일치하지 않습니다.");
//        }

        // 새로운 비밀번호 확인
        if (dto.getUserPw() != null && !dto.getUserPw().equals(dto.getUserConPw())) {
            return ResponseDto.setFailed("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 해싱
        String hashedPassword = dto.getUserPw() != null ? bCryptPasswordEncoder.encode(dto.getUserPw()) : user.getPassword();

        // UserEntity 수정
        user.update(dto, hashedPassword);

        try {
            // db에 사용자 저장
            userRepository.save(user);
        } catch (Exception e) {
            return ResponseDto.setFailed("데이터베이스 연결에 실패하였습니다.");
        }

        return ResponseDto.setSuccess("회원 정보가 성공적으로 수정되었습니다.");
    }

    public ResponseDto<?> delete(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isEmpty()) {
            return ResponseDto.setFailed("해당 회원을 찾을 수 없습니다.");
        }

        User user = optionalUser.get();
        try {
//            현재 유저의 학생정보를 모두 삭제
            studentRepository.deleteAll(user.getStudent());
//            현재 유저의 시간표를 모두 삭제
            scheduleRepository.deleteAll(user.getSchedule());
//            유저 삭제
            userRepository.delete(user);
            return ResponseDto.setSuccess("회원이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseDto.setFailed("데이터베이스 연결에 실패하였습니다: " + e.getMessage());
        }
    }

    public ResponseDto<?> userDetail(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isEmpty()) {
            return ResponseDto.setFailed("해당 회원을 찾을 수 없습니다.");
        }

        List<UserDetailDto> userView = optionalUser.stream()
                .map(user -> new UserDetailDto(
                        user.getUserId(),
                        user.getUsername(),
                        user.getUserNum(),
                        user.getUserDate(),
                        user.getUserJoin(),
                        user.getUserEmail(),
                        user.getSchoolName(),
                        user.getClassNum()
                ))
                .collect(Collectors.toList());
        return ResponseDto.setSuccessData("회원 정보를 성공적으로 불러왔습니다.", userView);
    }
}