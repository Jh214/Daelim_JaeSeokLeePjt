package jaeseok.jaeseoklee.service.user;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.user.find.FindUserIdBySmsCodeSend;
import jaeseok.jaeseoklee.dto.user.find.FindUserPasswordByUserNum;
import jaeseok.jaeseoklee.dto.user.find.VerificationPwSmsCode;
import jaeseok.jaeseoklee.dto.user.sms.OrgSendSms;
import jaeseok.jaeseoklee.dto.user.sms.ValidatePhoneNumAndSendKakao;
import jaeseok.jaeseoklee.dto.user.sms.VerificationSmsCode;


public interface SMS_KAKAO_Service {

    ResponseDto<?> orgSendSms(OrgSendSms orgSendSms);

    ResponseDto<?> FindUserIdSendKakao(FindUserIdBySmsCodeSend sendKakao);

    ResponseDto<?> validateAndSendKakao(ValidatePhoneNumAndSendKakao sendKakao);

    ResponseDto<?> sendKakao(FindUserPasswordByUserNum sendKakao);

    ResponseDto<?> verificationKakaoCode(VerificationSmsCode verCode);

    ResponseDto<?> findPwVerificationKakaoCode(VerificationPwSmsCode verCode);
}
