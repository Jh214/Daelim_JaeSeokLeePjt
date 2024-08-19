package jaeseok.jaeseoklee.entity.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeSlot {
    EightAM("08:00"),
    NineAM("09:00"),
    TenAM("10:00"),
    ElevenAM("11:00"),
    TwelvePM("12:00"),
    OnePM("13:00"),
    TwoPM("14:00"),
    ThreePM("15:00"),
    FourPM("16:00"),
    FivePM("17:00"),
    SixPM("18:00"),
    SevenPM("19:00"),
    EightPM("20:00"),
    NinePM("21:00"),
    TenPM("22:00");

    private final String description;
}