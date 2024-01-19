package GDG.whatssue.controller;

import GDG.whatssue.dto.schedule.ScheduleAddRequestDto;
import GDG.whatssue.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{clubId}/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity addSchedule(@PathVariable Long clubId, @RequestBody ScheduleAddRequestDto scheduleRequestDto) {

        scheduleService.createSchedule(clubId, scheduleRequestDto);

        return ResponseEntity.status(200).body("");
    }



}

