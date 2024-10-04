package jaeseok.jaeseoklee.dto.student;

import jaeseok.jaeseoklee.entity.student.Grade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRegisterDto {
    private String studentName;
    private String studentNum;
    private String studentDate;
    private String studentGender;
    private Grade studentGrade;
    private int classNum;
    private int studentCode;
    private String userId;
}
