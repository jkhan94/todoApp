package com.sparta.todoapp.service;

import com.sparta.todoapp.Repository.ScheduleRepository;
import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.dto.ScheduleResponseDto;
import com.sparta.todoapp.entity.Schedule;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(schedule);

        return scheduleResponseDto;
    }

    public List<ScheduleResponseDto> getSchedules() {
        return scheduleRepository.findAllByOrderByCreatedDateDesc().stream().map(ScheduleResponseDto::new).toList();
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto) {
        Schedule schedule = findSchedule(id);

        if (schedule.getPassword().equals(requestDto.getPassword())) {
            schedule.update(requestDto);

            schedule = findSchedule(id);; // 수정된 내용을 불러옴
            return new ScheduleResponseDto(schedule);
        } else {
            throw new IllegalArgumentException("수정할 수 없습니다.");
        }
    }

    public void deleteSchedule(Long id, String password) {
        // 해당 메모가 DB에 존재하는지 확인
        Schedule schedule = findSchedule(id);
        if (schedule.getPassword().equals(password)) {
            scheduleRepository.delete(schedule);
        } else {
            throw new IllegalArgumentException("삭제할 수 없습니다.");
        }
    }

    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 스케줄은 존재하지 않습니다."));
    }
}
