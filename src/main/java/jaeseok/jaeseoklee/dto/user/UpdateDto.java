package jaeseok.jaeseoklee.dto.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UpdateDto {
    private String userPw;
    private String userConPw;
    private String userName;
    private String userNum;
    private String userNickname;
    private String userEmail;
    private Integer schoolNum;
    private Integer class_Num;
}
