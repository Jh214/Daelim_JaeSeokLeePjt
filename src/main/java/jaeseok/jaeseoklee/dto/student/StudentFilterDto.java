package jaeseok.jaeseoklee.dto.student;

import jaeseok.jaeseoklee.entity.student.Grade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentFilterDto {
    private String userId;
    private Grade studentGrade;
    private int classNum;
}
