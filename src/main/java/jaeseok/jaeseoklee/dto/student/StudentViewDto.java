package jaeseok.jaeseoklee.dto.student;

import jaeseok.jaeseoklee.entity.Grade;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentViewDto {
    private Long studentId;
    private String studentName;
    private String studentNum;
    private String studentGender;
    private String studentDate;
    private Grade studentGrade;
    private int classNum;
    private int studentCode;
}
