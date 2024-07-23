package jaeseok.jaeseoklee.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class LoginDto {
    private String user_id;
    private String user_pw;
}
