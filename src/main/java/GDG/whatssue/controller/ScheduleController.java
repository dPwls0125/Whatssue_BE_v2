package GDG.whatssue.controller;

import GDG.whatssue.dto.schedule.AddScheduleRequestDto;
import GDG.whatssue.dto.schedule.GetScheduleResponseDto;
import GDG.whatssue.dto.schedule.ModifyScheduleRequestDto;
import GDG.whatssue.service.ScheduleService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{clubId}/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity addSchedule(@PathVariable(name = "clubId") Long clubId, @RequestBody AddScheduleRequestDto requestDto) {

        try{
            scheduleService.saveSchedule(clubId, requestDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Club Id");
        }

        return ResponseEntity.status(200).body("ok");

    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity getSchedule (@PathVariable(name = "clubId") Long clubId, @PathVariable(name = "scheduleId") Long scheduleId) {
        GetScheduleResponseDto scheduleDto;
        try{
             scheduleDto = scheduleService.findSchedule(scheduleId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Schedule Id");
        }

        return ResponseEntity.status(HttpStatus.OK).body(scheduleDto);
    }

    @PatchMapping("/{scheduleId}")
    public ResponseEntity modifySchedule(@PathVariable(name = "clubId") Long clubId, @PathVariable(name = "scheduleId") Long scheduleId,@RequestBody ModifyScheduleRequestDto requestDto) {
        try{
            scheduleService.updateSchedule(scheduleId, requestDto);
        } catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Schedule Id");
        }

        return ResponseEntity.status(HttpStatus.OK).body("ok");

    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity deleteSchedule(@PathVariable(name = "clubId") Long clubId, @PathVariable(name = "scheduleId") Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);

        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
}

