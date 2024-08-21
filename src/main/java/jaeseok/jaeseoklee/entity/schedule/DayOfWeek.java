package jaeseok.jaeseoklee.entity.schedule;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
public enum DayOfWeek {
    MonDay("월요일"),
    TuesDay("화요일"),
    WednesDay("수요일"),
    ThursDay("목요일"),
    FriDay("금요일");

    private final String changeToKorean;

    @JsonValue // changeToKorean 반환값으로 JSON 변환돼서 사용자에게 반환
    public String getChangeToKorean() {
        return changeToKorean;
    }
}
