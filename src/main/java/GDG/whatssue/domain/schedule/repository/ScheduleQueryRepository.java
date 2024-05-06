package GDG.whatssue.domain.schedule.repository;

import static GDG.whatssue.domain.schedule.entity.QSchedule.*;

import GDG.whatssue.domain.schedule.entity.Schedule;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Repository
@Transactional
@RequiredArgsConstructor
public class ScheduleQueryRepository {

    private final JPAQueryFactory query;

    public List<Schedule> findSchedules(Long clubId, String searchQuery, String startDate, String endDate) {
        return query
            .select(schedule)
            .from(schedule)
            .where(filterClub(clubId), filterQuery(searchQuery), filterDate(startDate, endDate))
            .fetch();
    }

    public BooleanExpression filterClub(Long clubId) {
        return schedule.club.id.eq(clubId);
    }

    public BooleanExpression filterQuery(String searchQuery) {
        if (StringUtils.hasText(searchQuery)) {
            return schedule.scheduleName.like("%" + searchQuery + "%");
        }

        return null;
    }

    public BooleanExpression filterDate(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime startDateTime = LocalDate.parse(startDate, formatter).atStartOfDay();
        LocalDateTime endDateTime = LocalDate.parse(endDate, formatter).atTime(LocalTime.MAX);

        return schedule.scheduleDateTime.between(startDateTime, endDateTime);
    }
}
