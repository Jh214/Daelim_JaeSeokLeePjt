package jaeseok.jaeseoklee.service;

import jaeseok.jaeseoklee.dto.*;
import jaeseok.jaeseoklee.entity.User;
import jaeseok.jaeseoklee.repository.UserRepository;
import jaeseok.jaeseoklee.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    public ResponseDto<?> signUp(SignUpDto dto) {
        String user_id = dto.getUser_id();
        String password = dto.getUser_pw();
        String confirmPassword = dto.getUser_conPw();

        // id 중복 확인
        if (userRepository.existsByUserId(user_id)) {
            return ResponseDto.setFailed("중복된 아이디입니다.");
        }

        // 비밀번호 확인
        if (!password.equals(confirmPassword)) {
            return ResponseDto.setFailed("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 해싱
        String hashedPassword = bCryptPasswordEncoder.encode(password);

        // UserEntity 생성
        User user = User.builder()
                .user_id(user_id)
                .user_pw(hashedPassword) // 해시된 비밀번호
                .user_name(dto.getUser_name())
                .user_num(dto.getUser_num())
                .user_date(dto.getUser_date())
                .user_nickname(dto.getUser_nickname())
                .user_join(LocalDateTime.now()) // 현재 시간으로 설정
                .user_email(dto.getUser_email())
                .school_num(dto.getSchool_num())
                .class_num(dto.getClass_num())
                .roles(List.of("ROLE_USER")) // 기본 역할 설정, 필요에 따라 수정 가능
                .build();

        try {
            // DB에 사용자 저장
            userRepository.save(user);
        } catch (Exception e) {
            return ResponseDto.setFailed("데이터베이스 연결에 실패하였습니다.");
        }

        return ResponseDto.setSuccess("회원 생성에 성공했습니다.");
    }

    public ResponseDto<?> login(LoginDto dto) {
        String userId = dto.getUser_id();
        String password = dto.getUser_pw();

        Optional<User> findUser = userRepository.findByUserId(userId);

        if (findUser.isPresent() && bCryptPasswordEncoder.matches(password, findUser.get().getUser_pw())) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    findUser.get().getAuthorities());
            JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(authentication);
            return ResponseDto.setSuccessData("로그인 성공", jwtTokenDto);
        } else {
            return ResponseDto.setFailed("아이디 또는 비밀번호를 확인해주세요.");
        }
    }

    public ResponseDto<?> logout() {
        // 리액트 클라이언트측에서 removeItem으로 토큰 제거 해주세용
        return ResponseDto.setSuccess("로그아웃 성공");
    }

    public ResponseDto<?> update(Long uid, UpdateDto dto) {
        Optional<User> userOptional = userRepository.findById(uid);
        if (!userOptional.isPresent()) {
            return ResponseDto.setFailed("해당 회원을 찾을 수 없습니다.");
        }

        User user = userOptional.get();

        // 비밀번호 확인
        if (dto.getUser_pw() != null && !bCryptPasswordEncoder.matches(dto.getUser_pw(), user.getPassword())) {
            return ResponseDto.setFailed("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새로운 비밀번호 확인
        if (dto.getUser_pw() != null && !dto.getUser_pw().equals(dto.getUser_conPw())) {
            return ResponseDto.setFailed("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 해싱
        String hashedPassword = dto.getUser_pw() != null ? bCryptPasswordEncoder.encode(dto.getUser_pw()) : user.getPassword();

        // UserEntity 수정
        User updatedUser = User.builder()
                .uid(user.getUid()) // 기존 uid를 유지
                .user_id(user.getUser_id()) // 기존 user_id를 유지
                .user_pw(hashedPassword)
                .user_name(dto.getUser_name() != null ? dto.getUser_name() : user.getUser_name())
                .user_num(dto.getUser_num() != null ? dto.getUser_num() : user.getUser_num())
                .user_nickname(dto.getUser_nickname() != null ? dto.getUser_nickname() : user.getUser_nickname())
                .user_join(user.getUser_join()) // 기존 가입일을 유지
                .user_email(dto.getUser_email() != null ? dto.getUser_email() : user.getUser_email())
                .school_num(dto.getSchool_num() != null ? dto.getSchool_num() : user.getSchool_num())
                .class_num(dto.getClass_num() != null ? dto.getClass_num() : user.getClass_num())
                .roles(user.getRoles()) // 기존 역할을 유지 (필요에 따라 업데이트 가능)
                .build();

        try {
            // DB에 사용자 저장
            userRepository.save(updatedUser);
        } catch (Exception e) {
            return ResponseDto.setFailed("데이터베이스 연결에 실패하였습니다.");
        }

        return ResponseDto.setSuccess("회원 정보가 성공적으로 수정되었습니다.");
    }

    public ResponseDto<?> delete(Long uid) {
        Optional<User> optionalUser = userRepository.findById(uid);
        if (!optionalUser.isPresent()) {
            return ResponseDto.setFailed("해당 회원을 찾을 수 없습니다.");
        }
        try {
            // DB에서 사용자 삭제
            userRepository.deleteById(uid);
        } catch (Exception e) {
            return ResponseDto.setFailed("데이터베이스 연결에 실패하였습니다.");
        }

        return ResponseDto.setSuccess("회원이 성공적으로 삭제되었습니다.");
    }
}