package jaeseok.jaeseoklee.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {
    private String userId;
    private String userPw;
    private String userConPw;
    private String userName;
    private String userNum;
    private String userDate;
    private String userNickname;
    private LocalDateTime userJoin;
    private String userEmail;
    private int schoolNum;
    private int classNum;
}
