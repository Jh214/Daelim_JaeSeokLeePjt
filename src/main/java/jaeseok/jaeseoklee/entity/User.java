package jaeseok.jaeseoklee.entity;

import jaeseok.jaeseoklee.dto.LoginDto;
import jaeseok.jaeseoklee.dto.SignUpDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Table(name = "user")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;
    @Column(unique = true, nullable = false, length = 50, name = "user_id")
    private String user_id;
    @Column(nullable = false)
    private String user_pw;
    @Column(nullable = false)
    private String user_name;
    @Column(unique = true, nullable = false)
    private String user_num;
    @Column(nullable = false)
    private String user_date;
    @Column(unique = true, nullable = false, length = 50)
    private String user_nickname;
    @Column(nullable = false)
    private LocalDateTime user_join;
    @Column(unique = true, nullable = false, length = 50)
    private String user_email;
    @Column(nullable = false)
    private int school_num;
    @Column(nullable = false)
    private int class_num;

    public User(SignUpDto dto) {
        this.user_id = dto.getUser_id();
        this.user_pw = dto.getUser_pw();
        this.user_name = dto.getUser_name();
        this.user_num = dto.getUser_num();
        this.user_date = dto.getUser_date();
        this.user_nickname = dto.getUser_nickname();
        this.user_join = LocalDateTime.now();
        this.user_email = dto.getUser_email();
        this.school_num = dto.getSchool_num();
        this.class_num = dto.getClass_num();
    }

    public User(LoginDto dto){
        this.user_id = dto.getUser_id();
        this.user_pw = dto.getUser_pw();
    }
}
