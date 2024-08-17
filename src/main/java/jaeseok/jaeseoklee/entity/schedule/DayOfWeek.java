package jaeseok.jaeseoklee.entity.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DayOfWeek {

    MonDay("월요일"),
    TuesDay("화요일"),
    WednesDay("수요일"),
    ThursDay("목요일"),
    FriDay("금요일");

    private final String description;
}
