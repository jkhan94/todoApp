package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.dto.ScheduleResponseDto;
import com.sparta.todoapp.exception.CommonResponse;
import com.sparta.todoapp.exception.CustomException;
import com.sparta.todoapp.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.todoapp.exception.ErrorEnum.NOT_VALID_ARGUMENTS;
import static com.sparta.todoapp.exception.ErrorEnum.SCHEDULE_NOT_FOUND;

// Swagger 링크: http://localhost:8080/swagger-ui/index.html#/
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
@Tag(name = "Schedule CRUD",
        description = "스케줄 등록, 조회, 수정, 삭제 컨트롤러.<br>" +
                "등록, 수정, 삭제는 인가된 사용자만 할 수 있음.<br>  " +
                "조회는 인가 없이 가능.")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Operation(summary = "스케줄 등록", description = "스케줄의 제목, 내용, 담당자, 비밀번호, 작성일을 전달받아 디비에 저장")
    @Parameters({
            @Parameter(name = "requestDto", description = "클라이언트가 입력한 스케줄 정보"),
            @Parameter(name = "bindingResult", description = "형식에 맞지 않는 입력값 때문에 발생하는 에러 저장"),
            @Parameter(name = "req", description = "HTTP 요청에 저장된 유저 정보 가져올 때 사용")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스케줄이 등록되었습니다."),
            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요.<br> 토큰이 유효하지 않습니다.<br> 회원을 찾을 수 없습니다.<br>")
//            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요."),
//            @ApiResponse(responseCode = "400", description = "토큰이 유효하지 않습니다."),
//            @ApiResponse(responseCode = "400", description = "회원을 찾을 수 없습니다.")
    })
    @PostMapping
    public ResponseEntity<CommonResponse> createSchedule(@RequestBody @Valid ScheduleRequestDto requestDto, BindingResult bindingResult,
                                                         HttpServletRequest req) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        // 오류가 발생했다면 어느 필드에서 에러가 발생했는지 출력
        if (!fieldErrors.isEmpty()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            throw new CustomException(NOT_VALID_ARGUMENTS);
        }
        ScheduleResponseDto schedule = scheduleService.createSchedule(requestDto, req);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .msg("스케줄이 등록되었습니다.")
                .data(schedule)
                .build());
    }


    @Operation(summary = "모든 스케줄 조회", description = "디비에 저장된 모든 스케줄을 조회")
    @ApiResponse(responseCode = "200", description = "등록된 모든 스케줄 조회.<br> 스케줄별로, 등록된 댓글도 출력")
    @GetMapping("/read")
    public List<ScheduleResponseDto> getSchedules() {
        return scheduleService.getSchedules();
    }


    @Operation(summary = "스케줄 수정", description = "입력받은 비밀번호와 디비의 비밀번호가 일치할 경우 해당 고유번호의 스케줄을 수정.")
    @Parameters({
            @Parameter(name = "scheduleId", description = "수정할 스케줄의 고유번호"),
            @Parameter(name = "requestDto", description = "클라이언트가 입력한 스케줄 정보"),
            @Parameter(name = "bindingResult", description = "형식에 맞지 않는 입력값 때문에 발생하는 에러 저장"),
            @Parameter(name = "req", description = "HTTP 요청에 저장된 유저 정보 가져올 때 사용")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스케줄이 수정되었습니다."),
            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요.<br> 등록되지 않은 스케줄입니다.<br>비밀번호가 틀립니다.<br>작성자만 삭제/수정할 수 있습니다.<br>" +
                    "토큰이 유효하지 않습니다.<br>회원을 찾을 수 없습니다.")
//            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요."),
//            @ApiResponse(responseCode = "400", description = "등록되지 않은 스케줄입니다."),
//            @ApiResponse(responseCode = "400", description = "비밀번호가 틀립니다."),
//            @ApiResponse(responseCode = "400", description = "작성자만 삭제/수정할 수 있습니다."),
//            @ApiResponse(responseCode = "400", description = "토큰이 유효하지 않습니다."),
//            @ApiResponse(responseCode = "400", description = "회원을 찾을 수 없습니다.")
    })
    @PutMapping("/{scheduleId}")
    public ResponseEntity<CommonResponse> updateSchedule(@PathVariable Long scheduleId, @Valid @RequestBody ScheduleRequestDto requestDto,
                                                         BindingResult bindingResult, HttpServletRequest req) {
        validateScheduleId(scheduleId);
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        // 오류가 발생했다면 어느 필드에서 에러가 발생했는지 출력
        if (!fieldErrors.isEmpty()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            throw new CustomException(NOT_VALID_ARGUMENTS);
        }
        ScheduleResponseDto schedule = scheduleService.updateSchedule(scheduleId, requestDto, req);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .msg("스케줄이 수정되었습니다.")
                .data(schedule)
                .build());
    }


    @Operation(summary = "스케줄 삭제",
            description = "입력받은 비밀번호와 디비의 비밀번호가 일치할 경우 해당 고유번호의 스케줄을 삭제." +
                    "스케줄에 달린 댓글도 같이 삭제")
    @Parameters({
            @Parameter(name = "scheduleId", description = "수정할 스케줄의 고유번호"),
            @Parameter(name = "requestDto", description = "클라이언트가 입력한 스케줄 정보"),
            @Parameter(name = "req", description = "HTTP 요청에 저장된 유저 정보 가져올 때 사용")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스케줄이 삭제되었습니다."),
            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요.<br> 등록되지 않은 스케줄입니다.<br>비밀번호가 틀립니다.<br>작성자만 삭제/수정할 수 있습니다.<br>" +
                    "토큰이 유효하지 않습니다.<br>회원을 찾을 수 없습니다."),
//            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요."),
//            @ApiResponse(responseCode = "400", description = "등록되지 않은 스케줄입니다."),
//            @ApiResponse(responseCode = "400", description = "비밀번호가 틀립니다."),
//            @ApiResponse(responseCode = "400", description = "작성자만 삭제/수정할 수 있습니다."),
//            @ApiResponse(responseCode = "400", description = "토큰이 유효하지 않습니다."),
//            @ApiResponse(responseCode = "400", description = "회원을 찾을 수 없습니다.")
    })
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<CommonResponse> deleteSchedule(@PathVariable Long scheduleId, @RequestBody ScheduleRequestDto requestDto, HttpServletRequest req) {
        validateScheduleId(scheduleId);
        scheduleService.deleteSchedule(scheduleId, requestDto, req);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .msg("스케줄이 삭제되었습니다.")
                .build());
    }


    private static void validateScheduleId(Long scheduleId) {
        if (scheduleId == null) {
            throw new CustomException(SCHEDULE_NOT_FOUND);
        }
    }

}
