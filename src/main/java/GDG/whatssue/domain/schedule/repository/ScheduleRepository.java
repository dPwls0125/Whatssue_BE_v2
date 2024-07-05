package GDG.whatssue.domain.schedule.repository;

import GDG.whatssue.domain.schedule.entity.AttendanceStatus;
import GDG.whatssue.domain.schedule.entity.Schedule;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {
    Optional<List<Schedule>>findByClub_Id(Long clubId);
    boolean existsByIdAndClub_Id(Long scheduleId, Long clubId);
    Optional<Schedule> findByIdAndClub_Id(Long scheduleId, Long clubId);
    Optional<List<Schedule>> findByClub_IdAndAttendanceStatus(Long clubId, AttendanceStatus attendanceStatus);
}