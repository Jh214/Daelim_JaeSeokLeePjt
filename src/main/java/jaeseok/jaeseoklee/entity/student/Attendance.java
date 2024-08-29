package jaeseok.jaeseoklee.entity.student;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Attendance {
    PRESENT("출석"),
    ABSENT("결석"),
    LATE("지각"),
    EARLY_LEAVE("조퇴");

    private final String description;

    @JsonValue
    public String getDescription() {
        return description;
    }
}
