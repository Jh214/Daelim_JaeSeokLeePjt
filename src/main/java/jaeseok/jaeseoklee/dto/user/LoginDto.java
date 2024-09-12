package jaeseok.jaeseoklee.dto.user;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    private String userId;
    private String userPw;
}
