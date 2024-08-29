package jaeseok.jaeseoklee.entity.schedule;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum DayOfWeek {
    MONDAY("월요일"),
    TUESDAY("화요일"),
    WEDNESDAY("수요일"),
    THURSDAY("목요일"),
    FRIDAY("금요일");

    private final String changeToKorean;

    @JsonValue // changeToKorean 반환값으로 JSON 변환돼서 사용자에게 반환
    public String getChangeToKorean() {
        return changeToKorean;
    }
}
