package jaeseok.jaeseoklee.service;

import jaeseok.jaeseoklee.dto.*;
import jaeseok.jaeseoklee.dto.jwt.JWTConfirmPasswordTokenDto;
import jaeseok.jaeseoklee.dto.jwt.JWTVerificationEmailCodeDto;
import jaeseok.jaeseoklee.dto.jwt.JwtTokenDto;
import jaeseok.jaeseoklee.dto.user.*;
import jaeseok.jaeseoklee.entity.User;
import jaeseok.jaeseoklee.repository.ScheduleRepository;
import jaeseok.jaeseoklee.repository.student.StudentRepository;
import jaeseok.jaeseoklee.repository.UserRepository;
import jaeseok.jaeseoklee.security.JwtTokenProvider;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final StudentRepository studentRepository;
    private final ScheduleRepository scheduleRepository;
    @Autowired
    private JavaMailSender emailSender;

    // ResponseDto를 반환하는 signUp(회원가입) 메서드
    public ResponseDto<?> signUp(SignUpDto dto) {
        String userId = dto.getUserId();
        String password = dto.getUserPw();
        String confirmPassword = dto.getUserConPw();
        String creationUserNumDash = dto.getUserNum().replaceAll("^(\\d{3})(\\d{4})(\\d{4})$", "$1-$2-$3");

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
                .userRealName(dto.getUserName())
                .userNum(creationUserNumDash) // 3번 째, 7번 째에 자동으로 - 생성
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

    public ResponseDto<?> checkId(String userId) {
        if (userRepository.existsByUserId(userId)) {
            return ResponseDto.setFailed("이미 등록된 아이디입니다.");
        }
        return ResponseDto.setSuccess("등록 가능한 아이디입니다.");
    }

    public ResponseDto<?> checkEmail(String userEmail) {
        if (userRepository.existsByUserEmail(userEmail)) {
            return ResponseDto.setFailed("이미 등록된 이메일 입니다.");
        }
        return ResponseDto.setSuccess("등록 가능한 이메일 입니다.");
    }

    public ResponseDto<?> checkNum(String userNum) {
        String creationUserNumDash = userNum.replaceAll("^(\\d{3})(\\d{4})(\\d{4})$", "$1-$2-$3");
        if (userRepository.existsByUserNum(creationUserNumDash)) {
            return ResponseDto.setFailed("이미 등록된 전화번호 입니다.");
        }
        return ResponseDto.setSuccess("등록 가능한 전화번호 입니다.");
    }

    public ResponseDto<?> login(LoginDto dto) {
        String userId = dto.getUserId();
        String password = dto.getUserPw();

        // UserId로 user 조회
        Optional<User> findUser = userRepository.findByUserId(userId);

        if (findUser.isPresent() && bCryptPasswordEncoder.matches(password, findUser.get().getUserPw())) {
            // 로그인 성공
            User user = findUser.get();
            String userName = user.getUserRealName(); // 사용자 이름 가져오기

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    user.getAuthorities()); // 사용자의 id와 비밀번호를 통해 권한정보를 가져옴
            JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(authentication); // JWT 토큰 생성

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("userName", userName);
            responseData.put("jwtToken", jwtTokenDto);

            String successMessage = "로그인 성공!";
            return ResponseDto.setSuccessData(successMessage, responseData);
        } else {
            // 로그인 실패
            return ResponseDto.setFailed("아이디 또는 비밀번호를 확인해주세요.");
        }
    }


    public ResponseDto<?> logout() {
        // 클라이언트측에서 removeItem으로 토큰 제거 해주세용
        return ResponseDto.setSuccess("로그아웃 성공");
    }

    public ResponseDto<?> update(String userId, UpdateDto dto, String token) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (!userOptional.isPresent()) {
            return ResponseDto.setFailed("해당 회원을 찾을 수 없습니다.");
        }
        // JWT 토큰 검증
        if (!jwtTokenProvider.validatePasswordVerificationToken(token)) {
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
    public ResponseDto<?> updatePassword(String userId, UpdatePasswrodDto dto, String token) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (!userOptional.isPresent()) {
            return ResponseDto.setFailed("해당 회원을 찾을 수 없습니다.");
        }
        // JWT 토큰 검증
        if (!jwtTokenProvider.validatePasswordVerificationToken(token)) {
            return ResponseDto.setFailed("권한이 없습니다.");
        }

        // 비밀번호 인증이 성공한 사용자인지 확인
        Authentication authentication = jwtTokenProvider.getPwAuthentication(token);
        if (!authentication.getName().equals(userId)) {
            return ResponseDto.setFailed("권한이 없습니다.");
        }
        User user = userOptional.get();

        if (!bCryptPasswordEncoder.matches(dto.getCurrentPw(), user.getUserPw())) {
            return ResponseDto.setFailed("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새로운 비밀번호 확인
        if (dto.getUserPw() != null && !dto.getUserPw().equals(dto.getUserConPw())) {
            return ResponseDto.setFailed("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 해싱
        String hashedPassword = bCryptPasswordEncoder.encode(dto.getUserPw());

        user.updatePassword(hashedPassword);

        try {
            // db에 사용자 저장
            userRepository.save(user);
        } catch (Exception e) {
            return ResponseDto.setFailed("데이터베이스 연결에 실패하였습니다.");
        }

        return ResponseDto.setSuccess("비밀번호가 성공적으로 수정되었습니다.");
    }

    public ResponseDto<?> confirmPw(String userId, ConfirmPasswordDto confirmPasswordDto) {
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
        JWTConfirmPasswordTokenDto jwtConfirmPasswordTokenDto = jwtTokenProvider.generatePasswordVerificationToken(authentication);
        return ResponseDto.setSuccessData("인증되었습니다", jwtConfirmPasswordTokenDto);
    }

    public ResponseDto<?> delete(String userId, String token) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isEmpty()) {
            return ResponseDto.setFailed("해당 회원을 찾을 수 없습니다.");
        }
        // JWT 토큰 검증
        if (!jwtTokenProvider.validatePasswordVerificationToken(token)) {
            return ResponseDto.setFailed("권한이 없습니다.");
        }

        // 비밀번호 인증 여부 확인
        Authentication authentication = jwtTokenProvider.getPwAuthentication(token);
        if (!authentication.getName().equals(userId)) {
            return ResponseDto.setFailed("권한이 없습니다.");
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
                        user.getClassNum()
                ))
                .collect(Collectors.toList());
        return ResponseDto.setSuccessData("회원 정보를 성공적으로 불러왔습니다.", userView);
    }


    private final ConcurrentMap<String, SendEmailDto> codeStore = new ConcurrentHashMap<>();

    public ResponseDto<?> sendFindPasswordEmailCode(SendEmailDto mailDto) {
        Optional<User> userOptional = userRepository.findByUserEmail(mailDto.getEmailAddr());

        if (!userOptional.isPresent()) {
            return ResponseDto.setFailed("존재하지 않는 사용자입니다.");
        }

        User user = userOptional.get();
        String mailAddr = mailDto.getEmailAddr();
        String userId = user.getUserId();

        if (!user.getUserEmail().equals(mailAddr)) {
            return ResponseDto.setFailed("등록된 이메일과 일치하지 않습니다.");
        }

        int ranCode = mailDto.getRanCode();
        LocalDateTime timeLimit = mailDto.getTimeLimit();

        SendEmailDto newSendEmailDto = new SendEmailDto(mailAddr, userId,ranCode, timeLimit);
        codeStore.put(mailAddr, newSendEmailDto);

        String mailContent = newSendEmailDto.getEmailContent();

        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("jsl@gosky.kr"); // 발신자
            helper.setTo(mailAddr); // 수신자
            helper.setSubject("[TeacHub] 메일인증 코드입니다."); // 제목
            helper.setText(mailContent, true); // 이메일 내용 설정

            // 이메일 전송
            emailSender.send(message);

        } catch (MessagingException | MailException e) {
            log.error("메일 전송 중 오류 발생: {}", e.getMessage(), e);
            return ResponseDto.setFailed("메일 전송에 실패하였습니다. 다시 시도해주세요.");
        }

        return ResponseDto.setSuccess("메일이 전송되었습니다.");
    }


    public ResponseDto<?> verificationEmailCode(VerificationCodeDto verCode) {
        Optional<User> userOptional = userRepository.findByUserEmail(verCode.getEmailAddr());
        String emailAddr = verCode.getEmailAddr();
        int inputCode = verCode.getInputCode();

        SendEmailDto sendEmailDto = codeStore.get(emailAddr);

        if (sendEmailDto == null) {
            return ResponseDto.setFailed("해당 이메일로 보낸 인증 코드가 없습니다.");
        }

        if (LocalDateTime.now().isAfter(sendEmailDto.getTimeLimit())) {
            codeStore.remove(emailAddr);  // 5분 지나면 코드 삭제
            return ResponseDto.setFailed("인증 코드가 만료되었습니다. 다시 시도해주세요.");
        }

        if (sendEmailDto.getRanCode() != inputCode) {
            return ResponseDto.setFailed("인증 코드가 일치하지 않습니다.");
        }

        User user = userOptional.get();

//        이 부분은 5분짜리 이메일인증토큰 생성 로직이 들어갈 예정
        Authentication authentication = new UsernamePasswordAuthenticationToken( // Authentication 객체 생성
                user.getUserId(),
                null,
                userOptional.get().getAuthorities()); // 사용자의 id와 비밀번호를 통해 권한정보를 가져옴
        JWTVerificationEmailCodeDto jwtVerificationEmailCodeDto = jwtTokenProvider.generateEmailCodeVerificationToken(authentication);

        codeStore.remove(emailAddr); // 인증 성공 후 코드를 삭제
        return ResponseDto.setSuccessData("인증이 성공적으로 완료되었습니다.", jwtVerificationEmailCodeDto);
    }

    public ResponseDto<?> findPassword(FindPasswordDto dto, String token) {
        String userId = dto.getUserId();
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (!userOptional.isPresent()) {
            return ResponseDto.setFailed("해당 회원을 찾을 수 없습니다.");
        }
        // JWT 토큰 검증
        if (!jwtTokenProvider.validateEmailVerificationToken(token)) {
            return ResponseDto.setFailed("권한이 없습니다.");
        }

        // 이메일 인증이 성공한 사용자인지 확인
        Authentication authentication = jwtTokenProvider.getEmailAuthentication(token);
        if (!authentication.getName().equals(userId)) {
            return ResponseDto.setFailed("권한이 없습니다.");
        }
        User user = userOptional.get();

        // 새로운 비밀번호 확인
        if (dto.getUserPw() != null && !dto.getUserPw().equals(dto.getUserConPw())) {
            return ResponseDto.setFailed("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 해싱
        String hashedPassword = bCryptPasswordEncoder.encode(dto.getUserPw());

        user.updatePassword(hashedPassword);

        try {
            // db에 사용자 저장
            userRepository.save(user);
        } catch (Exception e) {
            return ResponseDto.setFailed("데이터베이스 연결에 실패하였습니다.");
        }

        return ResponseDto.setSuccess("비밀번호가 성공적으로 변경되었습니다.");
    }

}
