package GDG.whatssue.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
@Slf4j
public class AttendanceController {
    @Operation(summary = "출석 열기_ADMIN")
    @PostMapping("/{scheduleId}/attendance/start")
    public ResponseEntity<?> openAttendance(@PathVariable Long scheduleId) {
        return null;
    }





//    @Operation(summary = "출석 닫기_ADMIN")

}
