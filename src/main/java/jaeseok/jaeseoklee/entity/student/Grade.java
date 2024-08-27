package jaeseok.jaeseoklee.entity.student;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Grade {
    firstGrade("1학년"),
    secondGrade("2학년"),
    thirdGrade("3학년"),
    fourthGrade("4학년"),
    fifthGrade("5학년"),
    sixthGrade("6학년");

    private final String gradeDescription;

    @JsonValue
    public String getGradeDescription() {
        return gradeDescription;
    }
}
