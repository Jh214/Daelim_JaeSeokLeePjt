package jaeseok.jaeseoklee.dto.student;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class StudentRegisterDto {
    private String studentName;
    private String studentNum;
    private String studentAge;
    private String studentGender;
    private int schoolNum;
    private int classNum;
    private Long uid;
}