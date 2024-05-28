package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.dto.ScheduleResponseDto;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.exception.CustomException;
import com.sparta.todoapp.repository.ScheduleRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sparta.todoapp.exception.ErrorEnum.*;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto, HttpServletRequest req) {
        Schedule schedule = new Schedule(requestDto);
        User user = (User) req.getAttribute("user");
        schedule.setUserSchedule(user);
        Schedule saveSchedule = scheduleRepository.save(schedule);
        // Entity -> ResponseDto
        return new ScheduleResponseDto(schedule);
    }

    public List<ScheduleResponseDto> getSchedules() {
        return scheduleRepository.findAllByOrderByCreatedDateDesc().stream().map(ScheduleResponseDto::new).toList();
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto, HttpServletRequest req) {
        Schedule schedule = findSchedule(id);
        User user = (User) req.getAttribute("user");

        checkPasswordUserid(requestDto, schedule, user);

        schedule.update(requestDto);
        schedule = findSchedule(id);
        // 수정된 내용을 불러옴
        return new ScheduleResponseDto(schedule);
    }

    public void deleteSchedule(Long id, ScheduleRequestDto requestDto, HttpServletRequest req) {
        // 해당 메모가 DB에 존재하는지 확인
        Schedule schedule = findSchedule(id);
        User user = (User) req.getAttribute("user");

        checkPasswordUserid(requestDto, schedule, user);

        scheduleRepository.delete(schedule);
    }

    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new CustomException(SCHEDULE_NOT_FOUND));
    }

    private static void checkPasswordUserid(ScheduleRequestDto requestDto, Schedule schedule, User user) {
        if (!schedule.getPassword().equals(requestDto.getPassword())) {
            throw new CustomException(BAD_PASSWORD);
        }
        if (schedule.getUserSchedule().getId() != user.getId()) {
            throw new CustomException(NOT_AVAILABLE_USER);
        }
    }
}
