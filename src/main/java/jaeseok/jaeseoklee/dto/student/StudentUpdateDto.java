package jaeseok.jaeseoklee.dto.student;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentUpdateDto {
    @NotNull
    private String studentName;
    @NotNull
    private String studentNum;
    @NotNull
    private int studentCode;
    @NotNull
    private String userId;
    @NotNull
    private Long studentId;
}
