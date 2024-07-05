package GDG.whatssue.domain.schedule.repository;

import static GDG.whatssue.domain.schedule.entity.QSchedule.*;

import GDG.whatssue.domain.schedule.dto.SchedulesResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Repository
@Transactional
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<SchedulesResponse> findAllScheduleDto(Long clubId, String searchQuery, LocalDate sDate, LocalDate eDate, Pageable pageable) {

        JPAQuery<SchedulesResponse> query = queryFactory
                .select(Projections.constructor(
                        SchedulesResponse.class,
                        schedule.id,
                        schedule.scheduleName,
                        schedule.attendanceStatus,
                        schedule.scheduleDate))
                .from(schedule)
                .where(
                        filterClub(clubId),
                        filterQuery(searchQuery),
                        filterDate(sDate, eDate))
                .orderBy(schedule.scheduleDate.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<SchedulesResponse> results = query.fetch();

        long total = query.select(schedule)
                .from(schedule)
                .where(
                        filterClub(clubId),
                        filterQuery(searchQuery),
                        filterDate(sDate, eDate))
                .stream()
                .count();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression filterClub(Long clubId) {
        return schedule.club.id.eq(clubId);
    }

    private BooleanExpression filterQuery(String searchQuery) {
        if (StringUtils.hasText(searchQuery)) {
            return schedule.scheduleName.like("%" + searchQuery + "%");
        }

        return null;
    }

    private BooleanExpression filterDate(LocalDate sDate, LocalDate eDate) {
        return schedule.scheduleDate.between(sDate.atStartOfDay(), LocalDateTime.of(eDate, LocalTime.MAX).withNano(0));
    }
}