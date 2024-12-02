package jaeseok.jaeseoklee.service.user;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.user.*;


public interface UserService {

    ResponseDto<?> signUp(SignUpDto dto);

    ResponseDto<?> checkId(UserIdDto userIdDto);

    ResponseDto<?> checkEmail(UserEmailDto userEmailDto);

    ResponseDto<?> login(LoginDto dto);

    ResponseDto<?> logout();

    ResponseDto<?> update(String userId, UpdateDto dto, String token);

    ResponseDto<?> updatePassword(String userId, UpdatePasswrodDto dto, String token);

    ResponseDto<?> confirmPw(ConfirmPasswordDto confirmPasswordDto);

    ResponseDto<?> delete(String userId, String token, UserPwDto userPwDto);

    ResponseDto<?> userDetail(String userId);

    ResponseDto<?> userList(String schoolName, String userId);

    ResponseDto<?> orgSendSmsInfo(String userId);
}
