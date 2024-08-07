package GDG.whatssue.domain.attendance.controller;

import GDG.whatssue.domain.attendance.dto.*;
import GDG.whatssue.domain.attendance.entity.AttendanceType;
import GDG.whatssue.domain.attendance.service.AttendanceService;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.global.common.annotation.ClubManager;
import GDG.whatssue.global.common.annotation.LoginUser;
import GDG.whatssue.global.error.CommonException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}/schedules")
@RestController
@Slf4j
public class AttendanceController {

    private final AttendanceService attendanceService;

    @ClubManager
    @Operation(summary = "출석 열기_manager ",description = "출석을 열면 출석을 진행하지 않았던 경우는 모두 결석 처리 리스트를 생성합니다.")
    @PostMapping("/{scheduleId}/attendance-start")
    public ResponseEntity openAttendance(@PathVariable("clubId") Long clubId, @PathVariable("scheduleId") Long scheduleId) {
        AttendanceNumResponseDto dto = attendanceService.openAttendance(clubId, scheduleId);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @ClubManager
    @Operation(summary = "출석 종료")
    @PostMapping("/{scheduleId}/attendance-end")
    public ResponseEntity offAttendance(@PathVariable Long clubId, @PathVariable Long scheduleId) {
        attendanceService.finishAttendanceOngoing(clubId, scheduleId);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @ClubManager
    @Operation(summary = "멤버들의 출석 진행 현황 리스트 조회")
    @GetMapping("/{scheduleId}/attendance-list")
    public ResponseEntity getAttendanceList( @PathVariable Long clubId, @PathVariable Long scheduleId){
        List<ScheduleAttendanceMemberDto> list =  attendanceService.getAttendanceList(scheduleId);
        Map<String,Object> response = new HashMap<>();
        response.put("data",list);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "출석하기 _ user")
    @PostMapping("/{scheduleId}/attendance")
    public ResponseEntity doAttendance(@PathVariable Long clubId, @PathVariable Long scheduleId, @LoginUser Long userId , @RequestBody AttendanceNumRequestDto requestDto) {
        attendanceService.doAttendance(clubId, scheduleId, userId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("출석이 완료되었습니다.");
    }

    @Operation(summary = "현재 출석 진행 중인 스케줄")
    @GetMapping("/attendance-ongoing")
    public ResponseEntity currentAttendanceList(@PathVariable Long clubId) {

        List<ScheduleDto> list = attendanceService.currentAttendanceList(clubId);
        System.out.println(list);
        Map<String,Object> response = new HashMap<>();
        response.put("data",list);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @Operation(summary = "출석 초기화")
    @GetMapping("/{scheduleId}/attendance-reset")
    public ResponseEntity resetAttendance( @PathVariable Long clubId, @PathVariable Long scheduleId ) {

        attendanceService.initAttendance(clubId, scheduleId);
        return ResponseEntity.status(HttpStatus.OK).body("출석이 초기화되었습니다.");

    }

    @ClubManager
    @Operation(summary = "출석 정정")
    @PutMapping("/attendance")
    public ResponseEntity<Void> modifyMemberAttendance(@PathVariable Long clubId, @RequestBody AttendModifyRequest request) {

        attendanceService.modifyMemberAttendance(clubId, request.getScheduleId(), request.getAttendmodifyDtoList());
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @GetMapping("/attendance/times")
    @Operation(summary = " 멤버의 출석/부재/공결 횟수 조회 ")
    public ResponseEntity getAttendanceTimes(@PathVariable Long clubId, @RequestParam Long memberId)
    {
        AttendanceTimesResponse response = attendanceService.getAttendanceTimes(clubId,memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/attendance/my-results")
    @Operation(summary = "본인 출석 필터링 결과 조회")
    public ResponseEntity getAttendanceResults(

            @LoginUser Long userId,
            @PathVariable Long clubId,
            @Parameter(description = "시작 날짜 (형식: YYYY-MM-DD)", required = true) @RequestParam("startDate") LocalDate startDate,
            @Parameter(description = "종료 날짜 (형식: YYYY-MM-DD)", required = true) @RequestParam("endDate") LocalDate endDate,
            @Parameter(description = "출석 검색 타입 (TOTAL, ATTENDANCE, ABSENCE, OFFICIAL_ABSENCE)", required = true)
            @RequestParam("attendanceType") String attendanceType,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "page", defaultValue = "0") int page

    ){
        MyAttendanceResultResponse response = attendanceService.getFilteredMyAttendance(userId, clubId, startDate, endDate, attendanceType,size,page);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping("/attendance/member/{memberId}")
    @Operation(summary = "매니저의 멤버별 출석 현황 조회")
    @ClubManager
    public ResponseEntity getMemberAttendanceResults(
            @PathVariable Long memberId,
            @PathVariable Long clubId,
            @Parameter(description = "시작 날짜 (형식: YYYY-MM-DD)", required = true) @RequestParam("startDate") LocalDate startDate,
            @Parameter(description = "종료 날짜 (형식: YYYY-MM-DD)", required = true) @RequestParam("endDate") LocalDate endDate,
            @Parameter(description = "출석 검색 타입 (TOTAL, ATTENDANCE, ABSENCE, OFFICIAL_ABSENCE)", required = true)
            @RequestParam("attendanceType") String attendanceType,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "page", defaultValue = "0") int page

    ){

        MyAttendanceResultResponse response = attendanceService.getFilteredMemberAttendance(memberId, clubId, startDate, endDate, attendanceType,size,page);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

}



