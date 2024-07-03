package GDG.whatssue.domain.attendance.repository;

import GDG.whatssue.domain.attendance.entity.AttendanceType;
import GDG.whatssue.domain.attendance.entity.ScheduleAttendanceResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleAttendanceResultRepository extends JpaRepository<ScheduleAttendanceResult, Long> {

    Optional<ScheduleAttendanceResult> findByScheduleIdAndClubMemberId(Long scheduleId, Long clubMemberId);
    List<ScheduleAttendanceResult> findByScheduleId(Long scheduleId);
    List<ScheduleAttendanceResult> findByScheduleIdAndAttendanceType(Long scheduleId, AttendanceType attendanceType);
    void deleteByScheduleId(Long scheduleId);


    @Query("select s from ScheduleAttendanceResult s where s.schedule.scheduleDate between :startDate and :endDate " +
            "and s.attendanceType = :attendanceType and s.clubMember.id = :memberId " +
            "order by s.schedule.scheduleDate asc")
    List<ScheduleAttendanceResult> findAllByScheduleDateBetweenAndAttendanceType(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("attendanceType") AttendanceType attendanceType,
            @Param("memberId") Long memberId
    );


    @Query("select s from ScheduleAttendanceResult s where s.schedule.scheduleDate between :startDate and :endDate " +
            "and s.clubMember.id = :memberId " +
            "order by s.schedule.scheduleDate asc")
    List<ScheduleAttendanceResult> findAllByScheduleDateBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("memberId") Long memberId
    );


}
