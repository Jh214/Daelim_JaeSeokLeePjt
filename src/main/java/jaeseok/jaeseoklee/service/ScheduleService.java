package jaeseok.jaeseoklee.service;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.schedule.ScheduleRegisterDto;
import jaeseok.jaeseoklee.dto.schedule.ScheduleUpdateDto;
import jaeseok.jaeseoklee.dto.schedule.ScheduleViewDto;
import jaeseok.jaeseoklee.entity.schedule.Schedule;
import jaeseok.jaeseoklee.entity.User;
import jaeseok.jaeseoklee.repository.ScheduleRepository;
import jaeseok.jaeseoklee.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    public ResponseDto<?> register(ScheduleRegisterDto registerDto) {
        Optional<User> userOptional = userRepository.findById(registerDto.getUid());
        if (!userOptional.isPresent()) {
            return ResponseDto.setFailed("잘못된 요청입니다.");
        }
        User user = userOptional.get();

        Schedule schedule = Schedule.builder()
                .subject(registerDto.getSubject())
                .location(registerDto.getLocation())
                .startTime(registerDto.getStartTime())
                .endTime(registerDto.getEndTime())
                .dayOfWeek(registerDto.getDayOfWeek())
                .user(user)
                .build();

        scheduleRepository.save(schedule);

        return ResponseDto.setSuccess("등록되었습니다.");
    }

    public ResponseDto<?> view(String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        List<Schedule> scheduleList = scheduleRepository.findByUserId(userId);
        if (!userOptional.isPresent()) {
            return ResponseDto.setFailed("잘못된 요청입니다.");
        }
        List<ScheduleViewDto> viewDto = scheduleList.stream()
                .map(this::convertToDto)
                .toList();

        return ResponseDto.setSuccessData("시간표를 성공적으로 불러왔습니다.", viewDto);
    }

    private ScheduleViewDto convertToDto(Schedule schedule) {
        return new ScheduleViewDto(
                schedule.getSubject(),
                schedule.getLocation(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getDayOfWeek()
        );
    }

    public ResponseDto<?> update(ScheduleUpdateDto updateDto) {
        User user = userRepository.findById(updateDto.getUid())
                .orElseThrow(() -> new RuntimeException("잘못된 요청입니다."));

        Schedule schedule = scheduleRepository.findById(updateDto.getScheduleId())
                .orElseThrow(() -> new RuntimeException("시간표를 불러오지 못했습니다."));

        if (!schedule.getUser().equals(user)) {
            return ResponseDto.setFailed("해당 교사의 시간표가 아닙니다.");
        }

        schedule.update(updateDto);

        try {
            scheduleRepository.save(schedule);
            return ResponseDto.setSuccess("시간표 수정이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseDto.setFailed("데이터베이스 연결에 실패하였습니다.");
        }
    }

    public ResponseDto<?> delete(Long scheduleId) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);

        if (!optionalSchedule.isPresent()) {
            return ResponseDto.setFailed("해당 시간표를 찾을 수 없습니다.");
        }

        try {
            scheduleRepository.deleteById(scheduleId);
        } catch (Exception e) {
            return ResponseDto.setFailed("데이터베이스 연결에 실패하였습니다.");
        }
        return ResponseDto.setSuccess("삭제되었습니다.");
    }
}