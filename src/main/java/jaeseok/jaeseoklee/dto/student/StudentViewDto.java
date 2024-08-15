package jaeseok.jaeseoklee.dto.student;

import lombok.*;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentViewDto {
    private String studentName;
    private String studentNum;
    private String studentGender;
    private String studentAge;
    private String schoolName;
    private int classNum;
}
