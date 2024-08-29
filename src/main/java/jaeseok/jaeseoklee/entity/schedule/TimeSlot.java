package jaeseok.jaeseoklee.entity.schedule;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeSlot {
    EIGHTAM("08:00"),
    NINEAM("09:00"),
    TENAM("10:00"),
    ELEVENAM("11:00"),
    TWELVEPM("12:00"),
    ONEPM("13:00"),
    TWOPM("14:00"),
    THREEPM("15:00"),
    FOURPM("16:00"),
    FIVEPM("17:00"),
    SIXPM("18:00"),
    SEVENPM("19:00"),
    EIGHTPM("20:00"),
    NINEPM("21:00"),
    TENPM("22:00");

    private final String description;

    @JsonValue // description 반환값으로 JSON 변환돼서 사용자에게 반환
    public String getDescription() {
        return description;
    }
}