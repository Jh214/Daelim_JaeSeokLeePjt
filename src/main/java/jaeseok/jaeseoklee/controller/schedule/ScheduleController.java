package jaeseok.jaeseoklee.controller.schedule;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.schedule.ScheduleRegisterDto;
import jaeseok.jaeseoklee.dto.schedule.ScheduleUpdateDto;
import jaeseok.jaeseoklee.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/register")
    public ResponseDto<?> registerSchedule(@RequestBody ScheduleRegisterDto registerDto) {
        ResponseDto<?> result = scheduleService.register(registerDto);

        return result;
    }

    @GetMapping("/view")
    public ResponseDto<?> viewSchedule(@RequestParam(name = "userId") String userId) {
        ResponseDto<?> result = scheduleService.view(userId);

        return result;
    }

    @PatchMapping("/update")
    public ResponseDto<?> updateSchedule(@RequestBody ScheduleUpdateDto updateDto) {
        ResponseDto<?> result = scheduleService.update(updateDto);

        return result;
    }

    @DeleteMapping("/delete")
    public ResponseDto<?> deleteSchedule(@RequestParam(name = "scheduleId") Long scheduleId) {
        ResponseDto<?> result = scheduleService.delete(scheduleId);

        return result;
    }
}
