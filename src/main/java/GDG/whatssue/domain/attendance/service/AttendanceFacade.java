package GDG.whatssue.domain.attendance.service;

import GDG.whatssue.domain.attendance.Error.AttendanceErrorCode;
import GDG.whatssue.domain.attendance.entity.AttendanceType;
import GDG.whatssue.domain.attendance.entity.ScheduleAttendanceResult;
import GDG.whatssue.domain.attendance.service.repository.ScheduleAttendanceResultRepository;
import GDG.whatssue.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttendanceFacade {

    private final ScheduleAttendanceResultRepository attendanceResultRepository;
    public ScheduleAttendanceResult getAttendanceResult(Long scheduleId, Long memberId){
        return attendanceResultRepository.findByScheduleIdAndClubMemberId(scheduleId, memberId)
                .orElseThrow(() -> new CommonException(AttendanceErrorCode.EX5100));
    }

    public List<ScheduleAttendanceResult> getAttendanceResult(Long memberId){
        // 찾은 객체가 없을 경우에는 빈 배열 반환
        return attendanceResultRepository.findByClubMemberId(memberId);
    }

    public List<ScheduleAttendanceResult> getAttendanceResultbySchedule(Long scheduleId, AttendanceType attendenceType){
        return attendanceResultRepository.findByScheduleIdAndAttendanceType(scheduleId, attendenceType);
    }

}
