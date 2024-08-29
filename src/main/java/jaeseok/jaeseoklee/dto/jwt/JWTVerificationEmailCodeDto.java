package jaeseok.jaeseoklee.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class JWTVerificationEmailCodeDto {
    private String grantType;
    private String accessEmailToken;
}
