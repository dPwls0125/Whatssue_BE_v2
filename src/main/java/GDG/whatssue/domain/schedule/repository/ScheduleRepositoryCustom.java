package GDG.whatssue.domain.schedule.repository;

import GDG.whatssue.domain.schedule.dto.SchedulesResponse;
import GDG.whatssue.domain.schedule.dto.SearchCond;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface ScheduleRepositoryCustom {
    PageImpl<SchedulesResponse> findAllSchedule(Long clubId, SearchCond searchCond, Pageable pageable);
}
