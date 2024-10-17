package jaeseok.jaeseoklee.dto.user.find;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationPwSmsCode {
    private String userNum;
    private int inputCode;
    private String userId;
}
