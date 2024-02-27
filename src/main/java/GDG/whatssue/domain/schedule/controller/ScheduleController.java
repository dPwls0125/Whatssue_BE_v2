package GDG.whatssue.domain.schedule.controller;

import GDG.whatssue.domain.schedule.dto.AddScheduleRequest;
import GDG.whatssue.domain.schedule.dto.GetScheduleResponse;
import GDG.whatssue.domain.schedule.dto.ModifyScheduleRequest;
import GDG.whatssue.domain.schedule.exception.NoScheduleException;
import GDG.whatssue.domain.schedule.exception.ScheduleErrorCode;
import GDG.whatssue.domain.schedule.service.ScheduleService;
import GDG.whatssue.global.error.ErrorResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    /**
     * Filter 또는 Interceptor를 통해 schedule이 club의 것인지 체크 필요 TODO
     */

    @ExceptionHandler(NoScheduleException.class)
    public ResponseEntity<ErrorResult> noScheduleExHandle(NoScheduleException e, HttpServletRequest request) {
        ScheduleErrorCode errorCode = (ScheduleErrorCode) e.getErrorCode();
        ErrorResult errorResult = new ErrorResult(errorCode.name(), errorCode.getMessage(e.getScheduleId()), request.getRequestURI());
        return new ResponseEntity<>(errorResult, errorCode.getHttpStatus());
    }



    @PostMapping
//    @PreAuthorize("hasRole('ROLE_'+#clubId+'MANAGER')")
    public ResponseEntity addSchedule(@PathVariable(name = "clubId") Long clubId,
        @Valid @RequestBody AddScheduleRequest requestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            //bean validation 처리 TODO
        }

        scheduleService.saveSchedule(clubId, requestDto);

        return ResponseEntity.status(200).body("ok");
    }

    @PatchMapping("/{scheduleId}")
//    @PreAuthorize("hasRole('ROLE_'+#clubId+'MANAGER')")
    public ResponseEntity modifySchedule(@PathVariable(name = "clubId") Long clubId, @PathVariable(name = "scheduleId") Long scheduleId,
        @Valid @RequestBody ModifyScheduleRequest requestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            //bean validation 처리 TODO
        }

        scheduleService.updateSchedule(scheduleId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @DeleteMapping("/{scheduleId}")
//    @PreAuthorize("hasRole('ROLE_'+#clubId+'MANAGER')")
    public ResponseEntity deleteSchedule(@PathVariable(name = "clubId") Long clubId, @PathVariable(name = "scheduleId") Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);

        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @GetMapping("/{scheduleId}")
//    @PreAuthorize("hasAnyRole('ROLE_'+#clubId+'MANAGER','ROLE_'+#clubId+'MEMBER')")
    public ResponseEntity getSchedule (@PathVariable(name = "clubId") Long clubId, @PathVariable(name = "scheduleId") Long scheduleId) {

        GetScheduleResponse scheduleDto = scheduleService.findSchedule(scheduleId);

        return ResponseEntity.status(HttpStatus.OK).body(scheduleDto);
    }

    //전체조회
    @GetMapping
//    @PreAuthorize("hasAnyRole('ROLE_'+#clubId+'MANAGER','ROLE_'+#clubId+'MEMBER')")
    public ResponseEntity getScheduleAll( @PathVariable(name = "clubId") Long clubId, @RequestParam(name = "date", required = false) String date,
        @RequestParam(name = "month", required = false) String month) {
        List<GetScheduleResponse> responseDtoList = scheduleService.findScheduleAllByFilter(clubId, date, month);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }


}

