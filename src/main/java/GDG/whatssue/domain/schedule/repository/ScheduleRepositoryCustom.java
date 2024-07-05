package GDG.whatssue.domain.schedule.repository;

import GDG.whatssue.domain.schedule.dto.SchedulesResponse;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleRepositoryCustom {
    Page<SchedulesResponse> findAllScheduleDto(Long clubId, String searchQuery, LocalDate sDate, LocalDate eDate, Pageable pageable);
}