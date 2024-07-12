package GDG.whatssue.domain.schedule.controller;

import static org.springframework.http.HttpStatus.*;

import GDG.whatssue.domain.schedule.dto.AddScheduleRequest;
import GDG.whatssue.domain.schedule.dto.AddScheduleResponse;
import GDG.whatssue.domain.schedule.dto.GetDateByScheduleExistResponse;
import GDG.whatssue.domain.schedule.dto.ScheduleDetailResponse;
import GDG.whatssue.domain.schedule.dto.ModifyScheduleRequest;
import GDG.whatssue.domain.schedule.dto.SchedulesResponse;
import GDG.whatssue.domain.schedule.service.ScheduleService;
import GDG.whatssue.global.common.annotation.ClubManager;
import GDG.whatssue.global.common.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ScheduleController", description = "모임의 일정에 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}/schedules")
@CrossOrigin
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * 일정 추가 api
     */
    @ClubManager
    @Operation(summary = "일정 추가", description = "날짜= yyyy-MM-dd, 시간= HH:mm")
    @PostMapping
    public ResponseEntity<AddScheduleResponse> addSchedule (
        @PathVariable(name = "clubId") Long clubId,
        @LoginUser Long userId,
        @Valid @RequestBody AddScheduleRequest requestDto) {

        return ResponseEntity
            .status(OK)
            .body(scheduleService.saveSchedule(clubId, userId, requestDto));
    }

    /**
     * 일정 수정 api
     */
    @ClubManager
    @Operation(summary = "일정 수정", description = "날짜 패턴 yyyy-MM-dd HH:ss")
    @PatchMapping("/{scheduleId}")
    public ResponseEntity<String> modifySchedule(
        @PathVariable(name = "clubId") Long clubId,
        @PathVariable(name = "scheduleId") Long scheduleId,
        @Valid @RequestBody ModifyScheduleRequest requestDto) {
        scheduleService.updateSchedule(clubId, scheduleId, requestDto);

        return ResponseEntity
            .status(OK)
            .body("ok");
    }

    /**
     * 일정 삭제 api
     */
    @ClubManager
    @Operation(summary = "일정 삭제")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<String> deleteSchedule(@PathVariable(name = "clubId") Long clubId, @PathVariable(name = "scheduleId") Long scheduleId) {
        scheduleService.deleteSchedule(clubId, scheduleId);

        return ResponseEntity
            .status(OK)
            .body("ok");
    }
    
    @Operation(summary = "일정 상세조회")
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDetailResponse> getSchedule (@PathVariable(name = "clubId") Long clubId, @PathVariable(name = "scheduleId") Long scheduleId) {
        ScheduleDetailResponse scheduleDto = scheduleService.getScheduleDetail(clubId, scheduleId);

        return ResponseEntity
            .status(OK)
            .body(scheduleDto);
    }

    @Operation(summary = "일정 목록조회(검색 : 검색어, 기간)")
    @GetMapping
    @Parameter(name = "keyword", description = "검색어. 일정명으로 검색", in = ParameterIn.QUERY)
    @Parameter(name = "startDate", description = "기간 시작일(yyyy-MM-dd). 미입력 시 1900년", in = ParameterIn.QUERY)
    @Parameter(name = "endDate", description = "기간 마지막일(yyyy-MM-dd). 미입력 시 2200년", in = ParameterIn.QUERY)
    public ResponseEntity<Page<SchedulesResponse>> getSchedules(
        @PathVariable(name = "clubId") Long clubId,
        @RequestParam(name = "keyword", required = false, defaultValue = "") String query,
        @RequestParam(name = "startDate", defaultValue = "1900-01-01") String startDate,
        @RequestParam(name = "endDate", required = false, defaultValue = "2199-12-31") String endDate,
        @RequestParam(name = "page", required = false, defaultValue = "0") int page,
        @RequestParam(name = "size", required = false, defaultValue = "20") int size) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate sDate = LocalDate.parse(startDate, formatter);
        LocalDate eDate = LocalDate.parse(endDate, formatter);

        return ResponseEntity
            .status(OK)
            .body(scheduleService.findAllSchedule(clubId, query, sDate, eDate, PageRequest.of(page, size)));
    }

    @Operation(summary = "일정이 존재하는 일자 조회(기간)")
    @GetMapping("/exist-date")
    @Parameter(name = "startDate", description = "기간 시작일(yyyy-MM-dd). 미입력 시 1900년", in = ParameterIn.QUERY)
    @Parameter(name = "endDate", description = "기간 마지막일(yyyy-MM-dd). 미입력 시 2200년", in = ParameterIn.QUERY)
    public ResponseEntity<Page<LocalDate>> getDateByScheduleExist(
        @PathVariable(name = "clubId") Long clubId,
        @RequestParam(name = "startDate", defaultValue = "1900-01-01") String startDate,
        @RequestParam(name = "endDate", required = false, defaultValue = "2199-12-31") String endDate,
        @RequestParam(name = "page", required = false, defaultValue = "0") int page,
        @RequestParam(name = "size", required = false, defaultValue = "40") int size) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate sDate = LocalDate.parse(startDate, formatter);
        LocalDate eDate = LocalDate.parse(endDate, formatter);

        return ResponseEntity
            .status(OK)
            .body(scheduleService.getDateByScheduleExist(clubId, sDate, eDate, PageRequest.of(page, size)));
    }
}