package GDG.whatssue.repository;

import GDG.whatssue.entity.ScheduleAttendanceResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleAttendanceResultRepository extends JpaRepository<ScheduleAttendanceResult, Long> {

    ScheduleAttendanceResult findByScheduleIdAndClubMemberId(Long scheduleId, Long clubMemberId);
}
