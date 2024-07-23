package jaeseok.jaeseoklee.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {
    private String user_id;
    private String user_pw;
    private String user_conPw;
    private String user_name;
    private String user_num;
    private String user_date;
    private String user_nickname;
    private LocalDateTime user_join;
    private String user_email;
    private int school_num;
    private int class_num;
}
