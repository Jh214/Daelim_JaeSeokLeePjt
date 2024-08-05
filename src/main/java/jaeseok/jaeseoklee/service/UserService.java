package jaeseok.jaeseoklee.service;

import jaeseok.jaeseoklee.dto.*;
import jaeseok.jaeseoklee.dto.user.LoginDto;
import jaeseok.jaeseoklee.dto.user.SignUpDto;
import jaeseok.jaeseoklee.dto.user.UpdateDto;
import jaeseok.jaeseoklee.entity.User;
import jaeseok.jaeseoklee.repository.UserRepository;
import jaeseok.jaeseoklee.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


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
                .userNickname(dto.getUserNickname())
                .userJoin(LocalDateTime.now()) // 현재 시간으로 설정
                .userEmail(dto.getUserEmail())
                .schoolNum(dto.getSchoolNum())
                .classNum(dto.getClassNum())
                .roles(List.of("USER")) // 기본 역할 설정, 필요에 따라 수정 가능
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
        String userId = dto.getUserId();
        String password = dto.getUserPw();

        Optional<User> findUser = userRepository.findByUserId(userId);

        if (findUser.isPresent() && bCryptPasswordEncoder.matches(password, findUser.get().getUserPw())) {
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

    public ResponseDto<?> update(String userId, UpdateDto dto) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (!userOptional.isPresent()) {
            return ResponseDto.setFailed("해당 회원을 찾을 수 없습니다.");
        }

        User user = userOptional.get();

        // 비밀번호 확인
        if (dto.getUserPw() != null && !bCryptPasswordEncoder.matches(dto.getUserPw(), user.getPassword())) {
            return ResponseDto.setFailed("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새로운 비밀번호 확인
        if (dto.getUserPw() != null && !dto.getUserPw().equals(dto.getUserConPw())) {
            return ResponseDto.setFailed("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 해싱
        String hashedPassword = dto.getUserPw() != null ? bCryptPasswordEncoder.encode(dto.getUserPw()) : user.getPassword();

        // UserEntity 수정
        User updatedUser = User.builder()
                .uid(user.getUid()) // 기존 uid를 유지
                .userId(user.getUserId()) // 기존 user_id를 유지
                .userPw(hashedPassword)
                .userName(dto.getUserName() != null ? dto.getUserName() : user.getUsername())
                .userNum(dto.getUserNum() != null ? dto.getUserNum() : user.getUserNum())
                .userNickname(dto.getUserNickname() != null ? dto.getUserNickname() : user.getUserNickname())
                .userJoin(user.getUserJoin()) // 기존 가입일을 유지
                .userEmail(dto.getUserEmail() != null ? dto.getUserEmail() : user.getUserEmail())
                .schoolNum(dto.getSchoolNum() != null ? dto.getSchoolNum() : user.getSchoolNum())
                .classNum(dto.getClass_Num() != null ? dto.getClass_Num() : user.getClassNum())
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

    public ResponseDto<?> delete(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if (!optionalUser.isPresent()) {
            return ResponseDto.setFailed("해당 회원을 찾을 수 없습니다.");
        }

        User user = optionalUser.get();

        try {
            user.getStudent().forEach(student -> student.setUser(null));
            user.getStudent().clear();
            // DB에서 사용자 삭제
            userRepository.deleteByUserId(userId);
            userRepository.flush();
        } catch (Exception e) {
            return ResponseDto.setFailed("데이터베이스 연결에 실패하였습니다.");
        }

        return ResponseDto.setSuccess("회원이 성공적으로 삭제되었습니다.");
    }

    public ResponseDto<?> userDetail(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isEmpty()) {
            return ResponseDto.setFailed("해당 회원을 찾을 수 없습니다.");
        }
        return ResponseDto.setSuccess("회원 정보를 성공적으로 불러왔습니다.");
    }
}