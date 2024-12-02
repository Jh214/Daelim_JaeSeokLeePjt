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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    public ResponseDto<?> register(ScheduleRegisterDto registerDto) {
        Optional<User> userOptional = userRepository.findByUserId(registerDto.getUserId());
        if (!userOptional.isPresent()) {
            return ResponseDto.setFailed("잘못된 요청입니다.");
        }

        User user = userOptional.get();

        boolean isDuplicate = scheduleRepository.existsByUserAndPeriodAndDayOfWeek(
                user,
                registerDto.getPeriod(),
                registerDto.getDayOfWeek()
        );

        if (isDuplicate) {
            return ResponseDto.setFailed("이미 해당 시간에 등록된 과목이 있습니다.");
        }

        Schedule schedule = Schedule.builder()
                .subject(registerDto.getSubject())
                .period(registerDto.getPeriod())
                .dayOfWeek(registerDto.getDayOfWeek())
                .user(user)
                .build();

        scheduleRepository.save(schedule);

        return ResponseDto.setSuccess("등록되었습니다.");
    }

    @Override
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
                schedule.getPeriod(),
                schedule.getDayOfWeek(),
                schedule.getScheduleId()
        );
    }

    @Override
    public ResponseDto<?> update(ScheduleUpdateDto updateDto) {
        User user = userRepository.findByUserId(updateDto.getUserId())
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

    @Override
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