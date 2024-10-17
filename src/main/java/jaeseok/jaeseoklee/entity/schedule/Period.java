package jaeseok.jaeseoklee.entity.schedule;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Period {
    FIRST_PERIOD("1교시"),
    SECOND_PERIOD("2교시"),
    THIRD_PERIOD("3교시"),
    FOURTH_PERIOD("4교시"),
    FIFTH_PERIOD("5교시"),
    SIXTH_PERIOD("6교시"),
    SEVENTH_PERIOD("7교시"),
    EIGHTH_PERIOD("8교시");

    private final String changeToKorean;

    @JsonValue // changeToKorean 반환값으로 JSON 변환돼서 사용자에게 반환
    public String getChangeToKorean() {
        return changeToKorean;
    }
}

