package jaeseok.jaeseoklee.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JWTConfirmPasswordTokenDto {
    private String grantType;
    private String accessPwToken;
}
