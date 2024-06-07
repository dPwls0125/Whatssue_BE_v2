package GDG.whatssue.domain.attendance.service;

import GDG.whatssue.domain.attendance.entity.ScheduleAttendanceResult;
import GDG.whatssue.domain.attendance.repository.ScheduleAttendanceResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttendanceFacade {

    private final ScheduleAttendanceResultRepository attendanceResultRepository;
    public  ScheduleAttendanceResult getAttendanceResult(Long scheduleId, Long memberId){
        return attendanceResultRepository.findByScheduleIdAndClubMemberId(scheduleId, memberId)
                .orElse(null);
    }
}
