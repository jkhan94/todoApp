package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.dto.ScheduleResponseDto;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.exception.CustomException;
import com.sparta.todoapp.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sparta.todoapp.exception.ErrorEnum.BAD_PASSWORD;
import static com.sparta.todoapp.exception.ErrorEnum.SCHEDULE_NOT_FOUND;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {
        Schedule schedule = new Schedule(requestDto);
        Schedule saveSchedule = scheduleRepository.save(schedule);
        // Entity -> ResponseDto
        return new ScheduleResponseDto(schedule);
    }

    public List<ScheduleResponseDto> getSchedules() {
        return scheduleRepository.findAllByOrderByCreatedDateDesc().stream().map(ScheduleResponseDto::new).toList();
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto) {
        Schedule schedule = findSchedule(id);

        if (schedule.getPassword().equals(requestDto.getPassword())) {
            schedule.update(requestDto);

            schedule = findSchedule(id);
            // 수정된 내용을 불러옴
            return new ScheduleResponseDto(schedule);
        } else {
            throw new CustomException(BAD_PASSWORD);
        }
    }

    public void deleteSchedule(Long id, String password) {
        // 해당 메모가 DB에 존재하는지 확인
        Schedule schedule = findSchedule(id);
        if (schedule.getPassword().equals(password)) {
            scheduleRepository.delete(schedule);
        } else {
            throw new CustomException(BAD_PASSWORD);
        }
    }

    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new CustomException(SCHEDULE_NOT_FOUND));
    }
}
