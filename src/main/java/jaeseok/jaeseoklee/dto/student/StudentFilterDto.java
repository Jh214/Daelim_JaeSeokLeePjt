package jaeseok.jaeseoklee.dto.student;

import jaeseok.jaeseoklee.entity.Grade;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentFilterDto {
    private String userId;
    private Grade studentGrade = null;
    private int classNum = 0;
    private int page = 0;
    private int size = 12;
}
