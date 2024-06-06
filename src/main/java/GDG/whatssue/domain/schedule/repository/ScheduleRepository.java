package GDG.whatssue.domain.schedule.repository;

import GDG.whatssue.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {
    Optional<Schedule> findById(Long id);
    Optional<List<Schedule>>findByClub_Id(Long clubId);
    boolean existsByIdAndClub_Id(Long scheduleId, Long clubId);
}
