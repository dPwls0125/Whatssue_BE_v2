package GDG.whatssue.controller;

import GDG.whatssue.dto.schedule.AddScheduleRequestDto;
import GDG.whatssue.dto.schedule.GetScheduleResponseDto;
import GDG.whatssue.dto.schedule.ModifyScheduleRequestDto;
import GDG.whatssue.service.ScheduleService;
import java.util.List;
import java.util.NoSuchElementException;

import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{clubId}/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    @Operation(summary = "스케줄 추가")
    public ResponseEntity addSchedule(@PathVariable(name = "clubId") Long clubId, @RequestBody AddScheduleRequestDto requestDto) {

        try{
            scheduleService.saveSchedule(clubId, requestDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Club Id");
        }

        return ResponseEntity.status(200).body("ok");

    }

    @PatchMapping("/{scheduleId}")
    @Operation(summary = "스케줄 수정")
    public ResponseEntity modifySchedule(@PathVariable(name = "clubId") Long clubId, @PathVariable(name = "scheduleId") Long scheduleId,@RequestBody ModifyScheduleRequestDto requestDto) {
        try{
            scheduleService.updateSchedule(scheduleId, requestDto);
        } catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Schedule Id");
        }

        return ResponseEntity.status(HttpStatus.OK).body("ok");

    }

    @DeleteMapping("/{scheduleId}")
    @Operation(summary = "스케줄 삭제")
    public ResponseEntity deleteSchedule(@PathVariable(name = "clubId") Long clubId, @PathVariable(name = "scheduleId") Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);

        return ResponseEntity.status(HttpStatus.OK).body("ok");
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

    //전체조회
    @GetMapping
    public ResponseEntity getScheduleAll(
        @PathVariable(name = "clubId") Long clubId,
        @RequestParam(name = "date", required = false) String date,
        @RequestParam(name = "month", required = false) String month) {
        List<GetScheduleResponseDto> responseDtoList = scheduleService.findScheduleAllByFilter(clubId, date, month);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }


}

