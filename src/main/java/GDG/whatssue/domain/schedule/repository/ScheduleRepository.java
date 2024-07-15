package GDG.whatssue.domain.schedule.repository;

import GDG.whatssue.domain.schedule.dto.SchedulesResponse;
import GDG.whatssue.domain.schedule.entity.AttendanceStatus;
import GDG.whatssue.domain.schedule.entity.Schedule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.swing.text.html.Option;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {
    Optional<List<Schedule>>findByClub_Id(Long clubId);
    boolean existsByIdAndClub_Id(Long scheduleId, Long clubId);
    Optional<Schedule> findByIdAndClub_Id(Long scheduleId, Long clubId);
    Optional<List<Schedule>> findByClub_IdAndAttendanceStatus(Long clubId, AttendanceStatus attendanceStatus);

    @Query("select distinct(s.scheduleDate)" +
        " from Schedule s" +
        " where (s.scheduleDate between :sDate and :eDate) and s.club.id = :clubId")
    Page<LocalDateTime> findDateByScheduleExist(@Param(value = "clubId") Long clubId, @Param(value = "sDate") LocalDateTime sDate, @Param(value = "eDate") LocalDateTime eDate, Pageable pageable);
}