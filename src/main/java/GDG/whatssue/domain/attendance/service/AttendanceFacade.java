package GDG.whatssue.domain.attendance.service;

import GDG.whatssue.domain.attendance.Error.AttendanceErrorCode;
import GDG.whatssue.domain.attendance.entity.AttendanceType;
import GDG.whatssue.domain.attendance.entity.ScheduleAttendanceResult;
import GDG.whatssue.domain.attendance.repository.ScheduleAttendanceResultRepository;
import GDG.whatssue.domain.schedule.entity.Schedule;
import GDG.whatssue.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttendanceFacade {

    private final ScheduleAttendanceResultRepository scheduleAttendanceResultRepository;
    public ScheduleAttendanceResult getAttendanceResult(Long scheduleId, Long memberId){
        return scheduleAttendanceResultRepository.findByScheduleIdAndClubMemberId(scheduleId, memberId)
                .orElseThrow(() -> new CommonException(AttendanceErrorCode.EX5100));
    }

    public List<ScheduleAttendanceResult> getAttendanceResultbySchedule(Long scheduleId){
        return scheduleAttendanceResultRepository.findByScheduleId(scheduleId);
    }

    public List<ScheduleAttendanceResult> getAttendanceResultbySchedule(Long scheduleId, AttendanceType attendenceType){
        return scheduleAttendanceResultRepository.findByScheduleIdAndAttendanceType(scheduleId, attendenceType);
    }

}
