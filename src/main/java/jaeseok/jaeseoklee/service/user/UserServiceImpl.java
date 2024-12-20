package jaeseok.jaeseoklee.service.user;

import jaeseok.jaeseoklee.dto.*;
import jaeseok.jaeseoklee.dto.jwt.JWTConfirmPasswordTokenDto;
import jaeseok.jaeseoklee.dto.jwt.JwtTokenDto;
import jaeseok.jaeseoklee.dto.user.*;
import jaeseok.jaeseoklee.dto.user.sms.OrgSendSmsInfo;
import jaeseok.jaeseoklee.entity.Grade;
import jaeseok.jaeseoklee.entity.User;
import jaeseok.jaeseoklee.entity.student.Student;
import jaeseok.jaeseoklee.repository.ScheduleRepository;
import jaeseok.jaeseoklee.repository.student.StudentRepository;
import jaeseok.jaeseoklee.repository.UserRepository;
import jaeseok.jaeseoklee.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final StudentRepository studentRepository;
    private final ScheduleRepository scheduleRepository;

    // 회원가입
    @Override
    public ResponseDto<?> signUp(SignUpDto dto) {
        String userId = dto.getUserId();
        String password = dto.getUserPw();
        String confirmPassword = dto.getUserConPw();
        String creationUserNumDash = dto.getUserNum().replaceAll("^(\\d{3})(\\d{4})(\\d{4})$", "$1-$2-$3");

        // 비밀번호 확인
        if (!password.equals(confirmPassword)) {
            return ResponseDto.setFailed("비밀번호가 일치하지 않습니다.");
        }
        if (userRepository.existsByUserId(userId)) {
            return ResponseDto.setFailed("이미 등록된 아이디입니다.");
        }
        if (userRepository.existsByUserEmail(dto.getUserEmail())) {
            return ResponseDto.setFailed("이미 등록된 이메일 입니다.");
        }
        if (userRepository.existsByUserNum(creationUserNumDash)) {
            return ResponseDto.setFailed("이미 등록된 전화번호 입니다.");
        }

        // 비밀번호 해싱
        String hashedPassword = bCryptPasswordEncoder.encode(password);

        // UserEntity 생성
        User user = User.builder()
                .userId(userId)
                .userPw(hashedPassword) // 해시된 비밀번호
                .userRealName(dto.getUserName())
                .userNum(creationUserNumDash) // 3번 째, 7번 째에 자동으로 - 생성
                .userDate(dto.getUserDate())
                .userJoin(LocalDateTime.now()) // 현재 시간으로 설정
                .userEmail(dto.getUserEmail())
                .schoolName(dto.getSchoolName())
                .classNum(dto.getClassNum())
                .grade(dto.getGrade())
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

    //    아이디 중복 검사
    @Override
    public ResponseDto<?> checkId(UserIdDto userIdDto) {
        String userId = userIdDto.getUserId();
        if (userRepository.existsByUserId(userId)) {
            return ResponseDto.setFailed("이미 등록된 아이디입니다.");
        }
        return ResponseDto.setSuccess("등록 가능한 아이디입니다.");
    }

    @Override
    public ResponseDto<?> checkEmail(UserEmailDto userEmailDto) {
        String userEmail = userEmailDto.getUserEmail();
        if (userRepository.existsByUserEmail(userEmail)) {
            return ResponseDto.setFailed("이미 등록된 이메일 입니다.");
        }
        return ResponseDto.setSuccess("등록 가능한 이메일입니다.");
    }

    //    로그인
    @Override
    public ResponseDto<?> login(LoginDto dto) {
        String userId = dto.getUserId();
        String password = dto.getUserPw();

        // UserId로 user 조회
        Optional<User> findUser = userRepository.findByUserId(userId);

        if (findUser.isPresent() && bCryptPasswordEncoder.matches(password, findUser.get().getUserPw())) {
            // 로그인 성공
            User user = findUser.get();
            String userName = user.getUserRealName(); // 사용자 이름 가져오기
            Grade userGrade = user.getGrade();
            int classNum = user.getClassNum();
            String schoolName = user.getSchoolName();


            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    user.getAuthorities()); // 사용자의 id와 비밀번호를 통해 권한정보를 가져옴
            JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(authentication, userId); // JWT 토큰 생성

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("userName", userName);
            responseData.put("grade", userGrade);
            responseData.put("classNum", classNum);
            responseData.put("jwtToken", jwtTokenDto);
            responseData.put("schoolName", schoolName);


            String successMessage = "로그인 성공!";
            return ResponseDto.setSuccessData(successMessage, responseData);
        } else {
            // 로그인 실패
            return ResponseDto.setFailed("아이디 또는 비밀번호를 확인해주세요.");
        }
    }


    @Override
    public ResponseDto<?> logout() {
        // 클라이언트측에서 removeItem으로 토큰 제거 해주세용
        return ResponseDto.setSuccess("로그아웃 성공");
    }

    //    회원수정
    @Override
    public ResponseDto<?> update(String userId, UpdateDto dto, String token) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (!userOptional.isPresent()) {
            return ResponseDto.setFailed("해당 회원을 찾을 수 없습니다.");
        }
        // JWT 토큰 검증
        if (!jwtTokenProvider.validatePasswordVerificationToken(token, userId)) {
            return ResponseDto.setFailed("권한이 없습니다.");
        }

        // 비밀번호 인증이 성공한 사용자인지 확인
        Authentication authentication = jwtTokenProvider.getPwAuthentication(token);
        if (!authentication.getName().equals(userId)) {
            return ResponseDto.setFailed("권한이 없습니다.");
        }
//        옵셔널로 찾은 userId 에 해당하는 User 정보로 UserEntity 생성
        User user = userOptional.get();

        String creationUserNumDash = dto.getUserNum().replaceAll("^(\\d{3})(\\d{4})(\\d{4})$", "$1-$2-$3");

        // 변경감지로 변경할 내용만 update 쿼리 적용
        user.update(dto, creationUserNumDash);

        try {
            // db에 사용자 저장
            userRepository.save(user);
        } catch (Exception e) {
            return ResponseDto.setFailed("데이터베이스 연결에 실패하였습니다.");
        }

        return ResponseDto.setSuccess("회원 정보가 성공적으로 수정되었습니다.");
    }

    //    비밀번호 수정
    @Override
    public ResponseDto<?> updatePassword(String userId, UpdatePasswrodDto dto, String token) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (!userOptional.isPresent()) {
            return ResponseDto.setFailed("해당 회원을 찾을 수 없습니다.");
        }
        // JWT 토큰 검증
        if (!jwtTokenProvider.validatePasswordVerificationToken(token, userId)) {
            return ResponseDto.setFailed("권한이 없습니다.");
        }

        // 비밀번호 인증이 성공한 사용자인지 확인
        Authentication authentication = jwtTokenProvider.getPwAuthentication(token);
        if (!authentication.getName().equals(userId)) {
            return ResponseDto.setFailed("권한이 없습니다.");
        }
        User user = userOptional.get();

        if (!bCryptPasswordEncoder.matches(dto.getCurrentPw(), user.getUserPw())) {
            log.info("currentPw = " + bCryptPasswordEncoder.encode(dto.getCurrentPw()));
            return ResponseDto.setFailed("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새로운 비밀번호 확인
        if (dto.getUserPw() != null && !dto.getUserPw().equals(dto.getUserConPw())) {
            return ResponseDto.setFailed("비밀번호가 일치하지 않습니다.");
        }
        // 비밀번호 해싱
        String hashedPassword = bCryptPasswordEncoder.encode(dto.getUserPw());

        if (hashedPassword.equals(user.getUserPw())) {
            return ResponseDto.setFailed("현재 사용중인 비밀번호 입니다.");
        }

        user.updatePassword(hashedPassword);

        try {
            // db에 사용자 저장
            userRepository.save(user);
        } catch (Exception e) {
            return ResponseDto.setFailed("데이터베이스 연결에 실패하였습니다.");
        }

        return ResponseDto.setSuccess("비밀번호가 성공적으로 수정되었습니다.");
    }

    //    현재 비밀번호 검증
    @Override
    public ResponseDto<?> confirmPw(ConfirmPasswordDto confirmPasswordDto) {
        String userId = confirmPasswordDto.getUserId();
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (!userOptional.isPresent()) {
            return ResponseDto.setFailed("해당 회원을 찾을 수 없습니다.");
        }

        User user = userOptional.get();

        // 비밀번호 확인
        if (confirmPasswordDto.getUserPw() != null && !bCryptPasswordEncoder.matches(confirmPasswordDto.getUserPw(), user.getPassword())) {
            return ResponseDto.setFailed("비밀번호가 일치하지 않습니다.");
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken( // Authentication 객체 생성
                user.getUserId(),
                null,
                userOptional.get().getAuthorities()); // 사용자의 id와 비밀번호를 통해 권한정보를 가져옴
        JWTConfirmPasswordTokenDto jwtConfirmPasswordTokenDto = jwtTokenProvider.generatePasswordVerificationToken(authentication, userId);
        return ResponseDto.setSuccessData("인증되었습니다", jwtConfirmPasswordTokenDto);
    }

    //    회원삭제
    @Override
    public ResponseDto<?> delete(String userId, String token, UserPwDto userPwDto) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        String userPw = userPwDto.getUserPw();
        if (optionalUser.isEmpty()) {
            return ResponseDto.setFailed("해당 회원을 찾을 수 없습니다.");
        }
        // JWT 토큰 검증
        if (!jwtTokenProvider.validatePasswordVerificationToken(token, userId)) {
            return ResponseDto.setFailed("권한이 없습니다.");
        }

        // 비밀번호 인증 여부 확인
        Authentication authentication = jwtTokenProvider.getPwAuthentication(token);
        if (!authentication.getName().equals(userId)) {
            return ResponseDto.setFailed("권한이 없습니다.");
        }
        log.info("no Hash userPw = " + userPw);
        User user = optionalUser.get();
        if (!bCryptPasswordEncoder.matches(userPw, user.getUserPw())) {
            log.info("userPw = " + bCryptPasswordEncoder.encode(userPw));
            log.info("user.getUserPw = " + user.getUserPw());
            return ResponseDto.setFailed("비밀번호가 일치하지 않습니다.");
        }

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

    //    회원정보
    @Override
    public ResponseDto<?> userDetail(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isEmpty()) {
            return ResponseDto.setFailed("해당 회원을 찾을 수 없습니다.");
        }
        String userNum = optionalUser.get().getUserNum().replaceAll("[^a-zA-Z0-9]", "");
//        정규식으로 특수문자 제거

        List<UserDetailDto> userView = optionalUser.stream()
                .map(user -> new UserDetailDto(
                        user.getUserId(),
                        user.getUserRealName(),
                        userNum,
                        user.getUserDate(),
                        user.getUserEmail(),
                        user.getSchoolName(),
                        user.getClassNum(),
                        user.getGrade()
                ))
                .collect(Collectors.toList());
        return ResponseDto.setSuccessData("회원 정보를 성공적으로 불러왔습니다.", userView);
    }

    @Override
    public ResponseDto<?> userList(String schoolName, String userId) {
        // schoolName으로 유저 목록 조회
        List<User> userList = userRepository.findUserBySchoolName(schoolName);
        log.info("schoolName = " + schoolName);

        if (userList.isEmpty()) {
            return ResponseDto.setFailed("회원이 없습니다.");
        }

        // 로그인된 userId와 일치하지 않는 유저들만 필터링하여 UserListViewDto로 변환
        List<UserListViewDto> userListDto = userList.stream()
                .filter(user -> !user.getUserId().equals(userId))  // 전달된 userId를 제외하고
                .map(user -> new UserListViewDto(user.getUserId(), user.getUserRealName()))
                .collect(Collectors.toList());

        if (userListDto.isEmpty()) {
            return ResponseDto.setFailed("해당 학교에 다른 회원이 없습니다.");
        }

        return ResponseDto.setSuccessData("회원 리스트 조회", userListDto);
    }

    @Override
    public ResponseDto<?> orgSendSmsInfo(String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);

        if (!userOptional.isPresent()) {
            return ResponseDto.setFailed("잘못된 요청입니다.");
        }

        // 학생 목록 조회 (List 타입)
        List<Student> students = studentRepository.findStudentListByUserId(userId);

        if (students.isEmpty()) {
            return ResponseDto.setFailed("조건에 맞는 학생 정보가 없습니다.");
        }

        List<OrgSendSmsInfo> studentViewDto = students.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseDto.setSuccessData("학생 정보를 불러왔습니다.", studentViewDto);
    }

    private OrgSendSmsInfo convertToDto(Student student) {
        return new OrgSendSmsInfo(
                student.getStudentName(),
                student.getStudentNum()
        );
    }

}
