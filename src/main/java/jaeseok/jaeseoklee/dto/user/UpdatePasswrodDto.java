package jaeseok.jaeseoklee.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswrodDto {
    private String userPw;
    private String userConPw;
}