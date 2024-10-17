package jaeseok.jaeseoklee.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Grade {
    FIRSTGRADE("1학년"),
    SECONDGRADE("2학년"),
    THIRDGRADE("3학년"),
    FOURTHGRADE("4학년"),
    FIFTHGRADE("5학년"),
    SIXTHGRADE("6학년");

    private final String gradeDescription;

    @JsonValue
    public String getGradeDescription() {
        return gradeDescription;
    }
}
