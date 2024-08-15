package jaeseok.jaeseoklee.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class ScheduleViewDto {
    private String scheduleSub;
    private String location;
}
