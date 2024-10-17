package jaeseok.jaeseoklee.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswrodDto {
    private String userPw;
    private String userConPw;
    private String currentPw;
}
