package jaeseok.jaeseoklee.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Grade {
    FIRSTGRADE(1),
    SECONDGRADE(2),
    THIRDGRADE(3),
    FOURTHGRADE(4),
    FIFTHGRADE(5),
    SIXTHGRADE(6);

    private final int gradeDescription;

    @JsonValue
    public int getGradeDescription() {
        return gradeDescription;
    }
}
