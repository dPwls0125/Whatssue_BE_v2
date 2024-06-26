package GDG.whatssue.domain.attendance.controller;

import GDG.whatssue.domain.attendance.dto.AttendanceNumRequestDto;
import GDG.whatssue.domain.attendance.dto.AttendanceNumResponseDto;
import GDG.whatssue.domain.attendance.dto.ScheduleAttendanceMemberDto;
import GDG.whatssue.domain.attendance.dto.ScheduleDto;
import GDG.whatssue.domain.attendance.service.AttendanceService;
import GDG.whatssue.global.common.annotation.ClubManager;
import GDG.whatssue.global.common.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}/schedules/{scheduleId}")
@RestController
@Slf4j
public class AttendanceController {

    private final AttendanceService attendanceService;
    @ClubManager
    @Operation(summary = "출석 열기_manager ",description = "출석을 열면 출석을 진행하지 않았던 경우는 모두 결석 처리 리스트를 생성합니다.")
    @PostMapping("/attendance-start")
    public ResponseEntity openAttendance(@PathVariable("clubId") Long clubId, @PathVariable("scheduleId") Long scheduleId) {
        AttendanceNumResponseDto dto = attendanceService.openAttendance(clubId, scheduleId);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @ClubManager
    @Operation(summary = "출석 종료")
    @PostMapping("/attendance-end")
    public ResponseEntity offAttendance(@PathVariable Long clubId, @PathVariable Long scheduleId) {
        try{
            attendanceService.finishAttendanceOngoing(clubId, scheduleId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @ClubManager
    @Operation(summary = "일정별 출석한 멤버 리스트 조회")
    @GetMapping("/attendance-list")
    public ResponseEntity getAttendanceList( @PathVariable Long clubId, @PathVariable Long scheduleId) {
        List<ScheduleAttendanceMemberDto> list =  attendanceService.getAttendanceList(scheduleId, clubId);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @Operation(summary = "출석하기 _ user")
    @PostMapping("/attendance/")
    public ResponseEntity doAttendance(@PathVariable Long clubId, @PathVariable Long scheduleId, @LoginUser Long userId , @RequestBody AttendanceNumRequestDto requestDto) {
        attendanceService.doAttendance(clubId, scheduleId, userId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("출석이 완료되었습니다.");
    }

    @Operation(summary = "현재 출석 진행 중인 스케줄")
    @GetMapping("/attendance-ongoing")
    public ResponseEntity currentAttendanceList(@PathVariable Long clubId, @PathVariable Long scheduleId) {
        List<ScheduleDto> list = attendanceService.currentAttendanceList(clubId,scheduleId);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @Operation(summary = "출석 초기화")
    @GetMapping("/attendance-reset")
    public ResponseEntity resetAttendance(@PathVariable Long clubId, @PathVariable Long scheduleId) {
        attendanceService.initAttendance(clubId, scheduleId);
        return ResponseEntity.status(HttpStatus.OK).body("출석이 초기화되었습니다.");
    }

    @ClubManager
    @Operation(summary = "출석 정정")
    @PutMapping("/attendance/{memberId}/{attendanceType}")
    public ResponseEntity modifyMemberAttendance(@PathVariable Long scheduleId, @PathVariable Long memberId, @PathVariable String attendanceType){
        try {
             attendanceService.modifyMemberAttendance(scheduleId, memberId, attendanceType);
         }catch (Exception e){
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
         }
         return ResponseEntity.status(HttpStatus.OK).body("출석이 정정되었습니다.");
    }


}



