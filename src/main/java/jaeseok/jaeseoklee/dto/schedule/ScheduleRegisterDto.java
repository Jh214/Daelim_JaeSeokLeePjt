package jaeseok.jaeseoklee.dto.schedule;

import jaeseok.jaeseoklee.entity.schedule.DayOfWeek;
import jaeseok.jaeseoklee.entity.schedule.TimeSlot;
import lombok.*;
import org.hibernate.internal.build.AllowNonPortable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleRegisterDto {
    private String subject;
    private String location;
    private TimeSlot startTime;
    private TimeSlot endTime;
    private DayOfWeek dayOfWeek;
    private Long uid;
}
