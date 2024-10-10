package jaeseok.jaeseoklee.dto.user.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationSmsCode {
    private String userNum;
    private int inputCode;
}
