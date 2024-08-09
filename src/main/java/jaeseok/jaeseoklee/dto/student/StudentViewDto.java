package jaeseok.jaeseoklee.dto.student;

import lombok.*;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentViewDto {
    private String studentName;
    private String studentNum;
    private String studentGender;
    private String studentAge;
    private String schoolName;
    private int classNum;
}
