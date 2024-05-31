package GDG.whatssue.domain.clubjoinrequest.repository;

import static GDG.whatssue.domain.clubjoinrequest.entity.QClubJoinRequest.clubJoinRequest;

import GDG.whatssue.domain.clubjoinrequest.dto.GetJoinRequestsResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ClubJoinRequestRepositoryImpl implements ClubJoinRequestRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<GetJoinRequestsResponse> findAllJoinRequest(Long userId, Pageable pageable) {
        JPAQuery<GetJoinRequestsResponse> query = queryFactory
            .select(
                Projections.constructor(
                    GetJoinRequestsResponse.class,
                    clubJoinRequest.id,
                    clubJoinRequest.club.id,
                    clubJoinRequest.club.clubName,
                    clubJoinRequest.status,
                    clubJoinRequest.updateAt
                ))
            .from(clubJoinRequest)
            .where(filterUser(userId))
            .orderBy(clubJoinRequest.createAt.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        List<GetJoinRequestsResponse> results = query.fetch();

        long total = queryFactory.select(clubJoinRequest)
            .from(clubJoinRequest)
            .where(filterUser(userId))
            .stream()
            .count();

        return new PageImpl<>(results, pageable, total);

    }

    private BooleanExpression filterUser(Long userId) {
        return clubJoinRequest.user.userId.eq(userId);
    }
}
