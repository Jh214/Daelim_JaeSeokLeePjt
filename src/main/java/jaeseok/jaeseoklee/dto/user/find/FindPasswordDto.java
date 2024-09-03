package jaeseok.jaeseoklee.dto.user.find;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FindPasswordDto {
    private String userId;
    private String userPw;
    private String userConPw;
}
