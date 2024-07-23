package jaeseok.jaeseoklee.service;

import jaeseok.jaeseoklee.dto.LoginDto;
import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.SignUpDto;
import jaeseok.jaeseoklee.entity.User;
import jaeseok.jaeseoklee.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public ResponseDto<?> signUp(SignUpDto dto) {
//        String user_id = dto.getUser_id();
        String password = dto.getUser_pw();
        String confirmPassword = dto.getUser_conPw();

        // id 중복 확인
//        try {
//            // 존재하는 경우 : true / 존재하지 않는 경우 : false
//            if(userRepository.existsByUserId(user_id)) {
//                return ResponseDto.setFailed("중복된 아이디입니다.");
//            }
//        } catch (Exception e) {
//            return ResponseDto.setFailed("데이터베이스 연결에 실패하였습니다.");
//        }

        // password 중복 확인
        if(!password.equals(confirmPassword)) {
            return ResponseDto.setFailed("비밀번호가 일치하지 않습니다.");
        }

        String hashedPassword = bCryptPasswordEncoder.encode(password);

        // UserEntity 생성
        User user = new User(dto);
        user = new User(
                user.getUid(),
                user.getUser_id(),
                hashedPassword, // 해시된 비밀번호
                user.getUser_name(),
                user.getUser_num(),
                user.getUser_date(),
                user.getUser_nickname(),
                user.getUser_join(),
                user.getUser_email(),
                user.getSchool_num(),
                user.getClass_num()
        );

        // UserRepository를 이용하여 DB에 Entity 저장 (데이터 적재)
        try {
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
            // 로그인 성공
            return ResponseDto.setSuccess("로그인 성공"); // 메인 페이지로 리다이렉션
        } else {
            // 로그인 실패
            return ResponseDto.setFailed("아이디 또는 비밀번호를 확인해주세요."); // 로그인 페이지로 리턴
        }
    }
}
