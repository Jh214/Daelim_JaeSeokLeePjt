package jaeseok.jaeseoklee.dto.user;

import jaeseok.jaeseoklee.entity.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpDto {
    private String userId;
    private String userPw;
    private String userConPw;
    private String userName;
    private String userNum;
    private String userDate;
//    @ApiModel(hidden = true)
    private LocalDateTime userJoin;
    private String userEmail;
    private String schoolName;
    private int classNum;
    private Grade grade;
}
