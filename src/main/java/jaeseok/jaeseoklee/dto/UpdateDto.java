package jaeseok.jaeseoklee.dto;

import jaeseok.jaeseoklee.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UpdateDto {
    private String user_pw;
    private String user_conPw;
    private String user_name;
    private String user_num;
    private String user_nickname;
    private String user_email;
    private Integer school_num;
    private Integer class_num;
}
