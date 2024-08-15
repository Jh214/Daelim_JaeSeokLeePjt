package jaeseok.jaeseoklee.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDto {
    private String userPw;
    private String userConPw;
    private String userName;
    private String userNum;
    private String userEmail;
    private String schoolName;
    private Integer class_Num;
}
