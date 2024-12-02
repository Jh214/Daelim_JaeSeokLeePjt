package jaeseok.jaeseoklee.service;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.schedule.ScheduleRegisterDto;
import jaeseok.jaeseoklee.dto.schedule.ScheduleUpdateDto;

public interface ScheduleService {

    ResponseDto<?> register(ScheduleRegisterDto registerDto);

    ResponseDto<?> view(String userId);

    ResponseDto<?> update(ScheduleUpdateDto updateDto);

    ResponseDto<?> delete(Long scheduleId);
}
