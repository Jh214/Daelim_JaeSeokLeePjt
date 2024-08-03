package jaeseok.jaeseoklee.dto.user;

import lombok.*;

@Data
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class LoginDto {
    private String userId;
    private String userPw;
}
