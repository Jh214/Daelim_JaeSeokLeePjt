package jaeseok.jaeseoklee.service.user;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.user.find.FindPasswordDto;
import jaeseok.jaeseoklee.dto.user.find.FindUserIdSendEmailDto;
import jaeseok.jaeseoklee.dto.user.find.SendEmailDto;
import jaeseok.jaeseoklee.dto.user.find.VerificationCodeDto;


public interface FindUserService {

    ResponseDto<?> sendFindPasswordEmailCode(SendEmailDto mailDto);

    ResponseDto<?> verificationEmailCode(VerificationCodeDto verCode);

    ResponseDto<?> findPassword(FindPasswordDto dto, String token);

    ResponseDto<?> sendFindUserIdEmailCode(FindUserIdSendEmailDto findUserIdSendEmailDto);


}
