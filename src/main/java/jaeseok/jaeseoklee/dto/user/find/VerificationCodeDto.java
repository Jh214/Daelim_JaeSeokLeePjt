package jaeseok.jaeseoklee.dto.user.find;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCodeDto {
    private String userEmail;
    private int inputCode;
}
