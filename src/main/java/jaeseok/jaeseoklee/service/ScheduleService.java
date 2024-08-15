package jaeseok.jaeseoklee.service;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.schedule.ScheduleRegisterDto;
import jaeseok.jaeseoklee.dto.schedule.ScheduleViewDto;
import jaeseok.jaeseoklee.dto.student.StudentViewDto;
import jaeseok.jaeseoklee.entity.Schedule;
import jaeseok.jaeseoklee.entity.Student;
import jaeseok.jaeseoklee.entity.User;
import jaeseok.jaeseoklee.repository.ScheduleRepository;
import jaeseok.jaeseoklee.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    public ResponseDto<?> register(ScheduleRegisterDto registerDto){
        Optional<User> userOptional = userRepository.findById(registerDto.getUid());
        if (!userOptional.isPresent()){
            return ResponseDto.setFailed("잘못된 요청입니다.");
        }
        User user = userOptional.get();

        Schedule schedule = Schedule.builder()
                .scheduleSub(registerDto.getScheduleSub())
                .location(registerDto.getLocation())
                .user(user)
                .build();

        scheduleRepository.save(schedule);

        return ResponseDto.setSuccess("과목이 등록되었습니다.");
    }

    public ResponseDto<?> view(String userId){
        Optional<User> userOptional = userRepository.findByUserId(userId);
        List<Schedule> scheduleList = scheduleRepository.findByUserId(userId);
        if (!userOptional.isPresent()){
            return ResponseDto.setFailed("잘못된 요청입니다.");
        }
        List<ScheduleViewDto> viewDto = scheduleList.stream()
                .map(this::convertToDto)
                .toList();

        return ResponseDto.setSuccessData("시간표를 성공적으로 불러왔습니다.", viewDto);
    }

    private ScheduleViewDto convertToDto(Schedule schedule) {
        return new ScheduleViewDto(
                schedule.getScheduleSub(),
                schedule.getLocation()
        );
    }}
