package GDG.whatssue.controller;

import GDG.whatssue.dto.schedule.AttendanceNumResponseDto;
import GDG.whatssue.service.AttendanceService;
import GDG.whatssue.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/{clubId}/schedules/{scheduleId}")
@Slf4j
public class AttendanceController {
    private final AttendanceService attendanceService;

    @Operation(summary = "출석 열기_ADMIN")
    @GetMapping("/attendance-start")
    public ResponseEntity openAttendance(@PathVariable Long clubId, @PathVariable Long scheduleId) {
        AttendanceNumResponseDto dto;
        try {
            dto = attendanceService.openAttendance(clubId, scheduleId);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @Operation(summary = "출석 종료")
    @PostMapping("/attendance-end")
    public ResponseEntity<?> offAttendance(@PathVariable Long clubId, @PathVariable Long scheduleId) {
        try{
            attendanceService.deleteAttendance(clubId, scheduleId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @Operation(summary = "출석 재시작")
    @PostMapping("/attendance-restart")
    public ResponseEntity<?> restartAttendance(@PathVariable Long scheduleId) {
        return null;
    }
}



