package jaeseok.jaeseoklee.service.user;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.jwt.JWTVerificationEmailCodeDto;
import jaeseok.jaeseoklee.dto.user.find.FindPasswordDto;
import jaeseok.jaeseoklee.dto.user.find.FindUserIdSendEmailDto;
import jaeseok.jaeseoklee.dto.user.find.SendEmailDto;
import jaeseok.jaeseoklee.dto.user.find.VerificationCodeDto;
import jaeseok.jaeseoklee.entity.User;
import jaeseok.jaeseoklee.repository.UserRepository;
import jaeseok.jaeseoklee.security.JwtTokenProvider;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FindUserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    private JavaMailSender emailSender;

    private final ConcurrentMap<String, SendEmailDto> codeStore = new ConcurrentHashMap<>();

//    비밀번호 찾기 이메일 인증코드 전송
    public ResponseDto<?> sendFindPasswordEmailCode(SendEmailDto mailDto) {
        Optional<User> userOptional = userRepository.findByUserId(mailDto.getUserId());

        if (!userOptional.isPresent()) {
            return ResponseDto.setFailed("존재하지 않는 사용자입니다.");
        }

        User user = userOptional.get();
        String mailAddr = mailDto.getUserEmail();
        String userId = user.getUserId();

        if (!user.getUserEmail().equals(mailAddr)) {
            return ResponseDto.setFailed("등록된 이메일과 일치하지 않습니다.");
        }

        int ranCode = mailDto.getRanCode();
        LocalDateTime timeLimit = mailDto.getTimeLimit();

        SendEmailDto newSendEmailDto = new SendEmailDto(mailAddr, userId,ranCode, timeLimit);
        codeStore.put(mailAddr, newSendEmailDto);
        codeStore.put(userId, newSendEmailDto);

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

//    이메일 인증코드 검증
    public ResponseDto<?> verificationEmailCode(VerificationCodeDto verCode) {
        String emailAddr = verCode.getUserEmail();
        String userId = verCode.getUserId();
        Optional<User> userOptional = userRepository.findByUserEmail(emailAddr);
        int inputCode = verCode.getInputCode();

        SendEmailDto sendEmailDto = codeStore.get(emailAddr);
        SendEmailDto sendEmailIdDto = codeStore.get(userId);

        if (sendEmailDto == null || sendEmailIdDto == null) {
            return ResponseDto.setFailed("해당 아이디가 잘못되었거나 이메일로 보낸 인증 코드가 없습니다.");
        }

        if (LocalDateTime.now().isAfter(sendEmailDto.getTimeLimit())) {
            codeStore.remove(emailAddr);  // 5분 지나면 코드 삭제
            return ResponseDto.setFailed("인증 코드가 만료되었습니다. 다시 시도해주세요.");
        }

        if (sendEmailDto.getRanCode() != inputCode) {
            return ResponseDto.setFailed("인증 코드가 일치하지 않습니다.");
        }

        User user = userOptional.get();

//        5분짜리 이메일인증토큰 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken( // Authentication 객체 생성
                user.getUserId(),
                null,
                userOptional.get().getAuthorities()); // 사용자의 id와 비밀번호를 통해 권한정보를 가져옴
        JWTVerificationEmailCodeDto jwtVerificationEmailCodeDto = jwtTokenProvider.generateEmailCodeVerificationToken(authentication);

        codeStore.remove(emailAddr); // 인증 성공 후 코드를 삭제
        return ResponseDto.setSuccessData("인증이 성공적으로 완료되었습니다.", jwtVerificationEmailCodeDto);
    }

//    비밀번호 찾기(변경)
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

        return ResponseDto.setSuccess("비밀번호가 변경되었습니다.");
    }

    //    비밀번호 찾기 이메일 인증코드 전송
    public ResponseDto<?> sendFindUserIdEmailCode(FindUserIdSendEmailDto findUserIdSendEmailDto) {
        Optional<User> userOptional = userRepository.findByUserEmail(findUserIdSendEmailDto.getUserEmail());

        if (!userOptional.isPresent()) {
            return ResponseDto.setFailed("존재하지 않는 사용자입니다.");
        }

        User user = userOptional.get();
        String mailAddr = findUserIdSendEmailDto.getUserEmail();
        String userId = user.getUserId();

        if (!user.getUserEmail().equals(mailAddr)) {
            return ResponseDto.setFailed("등록된 이메일과 일치하지 않습니다.");
        }

        LocalDateTime timeLimit = findUserIdSendEmailDto.getTimeLimit();

        FindUserIdSendEmailDto newFindUserIdSendEmailDto = new FindUserIdSendEmailDto(mailAddr, userId, timeLimit);

        String mailContent = newFindUserIdSendEmailDto.getEmailContent();

        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("jsl@gosky.kr"); // 발신자
            helper.setTo(mailAddr); // 수신자
            helper.setSubject("[TeacHub] 회원님의 아이디 입니다."); // 제목
            helper.setText(mailContent, true); // 이메일 내용 설정

            // 이메일 전송
            emailSender.send(message);

        } catch (MessagingException | MailException e) {
            log.error("메일 전송 중 오류 발생: {}", e.getMessage(), e);
            return ResponseDto.setFailed("메일 전송에 실패하였습니다. 다시 시도해주세요.");
        }

        return ResponseDto.setSuccess("메일이 전송되었습니다.");
    }
}
