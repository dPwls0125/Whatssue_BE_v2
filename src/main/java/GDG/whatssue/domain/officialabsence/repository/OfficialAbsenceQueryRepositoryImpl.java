package GDG.whatssue.domain.officialabsence.repository;

import GDG.whatssue.domain.officialabsence.dto.OfficialAbsenceGetRequestDto;
import GDG.whatssue.domain.officialabsence.entity.QOfficialAbsenceRequest;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequestType;
import GDG.whatssue.domain.member.entity.QClubMember;
import GDG.whatssue.domain.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OfficialAbsenceQueryRepositoryImpl implements OfficialAbsenceQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public OfficialAbsenceQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<OfficialAbsenceGetRequestDto> findOfficialAbsenceRequests(Long userId, Long clubId, OfficialAbsenceRequestType requestType, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        QOfficialAbsenceRequest officialAbsenceRequest = QOfficialAbsenceRequest.officialAbsenceRequest;
        QClubMember clubMember = QClubMember.clubMember;
        QUser user = QUser.user;

        var query = queryFactory.select(Projections.constructor(OfficialAbsenceGetRequestDto.class,
                        officialAbsenceRequest.id,
                        officialAbsenceRequest.clubMember.id,
                        officialAbsenceRequest.schedule.id,
                        officialAbsenceRequest.schedule.scheduleName,
                        officialAbsenceRequest.schedule.scheduleDate,
                        officialAbsenceRequest.officialAbsenceContent,
                        officialAbsenceRequest.officialAbsenceRequestType,
                        officialAbsenceRequest.createAt,
                        officialAbsenceRequest.updateAt))
                .from(officialAbsenceRequest)
                .join(officialAbsenceRequest.clubMember, clubMember)
                .join(clubMember.user, user)
                .where(
                        officialAbsenceRequest.clubMember.club.id.eq(clubId)
                                .and(user.userId.eq(userId))
                                .and(officialAbsenceRequest.schedule.scheduleDate.between(startDate, endDate))
                );

        // requestType != null 조건에만 필터링 적용
        if (requestType != null) {
            query = query.where(officialAbsenceRequest.officialAbsenceRequestType.eq(requestType));
        }

        // Fetch results with pagination
        List<OfficialAbsenceGetRequestDto> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Fetch total count without pagination
        long total = queryFactory.selectFrom(officialAbsenceRequest)
                .join(officialAbsenceRequest.clubMember, clubMember)
                .join(clubMember.user, user)
                .where(
                        officialAbsenceRequest.clubMember.club.id.eq(clubId)
                                .and(user.userId.eq(userId))
                                .and(officialAbsenceRequest.schedule.scheduleDate.between(startDate, endDate))
                )
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }
}
