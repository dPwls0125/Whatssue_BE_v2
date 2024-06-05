package GDG.whatssue.domain.member.repository;

import static GDG.whatssue.domain.club.entity.QClub.*;
import static GDG.whatssue.domain.member.entity.QClubMember.clubMember;
import static com.querydsl.core.types.ExpressionUtils.count;

import GDG.whatssue.domain.clubjoinrequest.dto.GetJoinClubResponse;
import GDG.whatssue.domain.member.entity.QClubMember;
import GDG.whatssue.global.util.S3Utils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ClubMemberRepositoryImpl implements ClubMemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<GetJoinClubResponse> getJoinClubList(Long userId, Pageable pageable) {
        QClubMember subClubMember = new QClubMember("subClubMember"); //서브쿼리를 위한 구분

        JPAQuery<GetJoinClubResponse> query = queryFactory.select(
                Projections.constructor(
                    GetJoinClubResponse.class,
                    clubMember.club.id.as("clubId"),
                    clubMember.club.clubName,
                    clubMember.club.profileImage.storeFileName,
                    clubMember.createAt,
                    clubMember.role,
                    ExpressionUtils.as( //memberCount 서브쿼리
                        JPAExpressions.select(count(subClubMember))
                            .from(subClubMember)
                            .where(subClubMember.club.eq(clubMember.club)), "memberCount"
                    )
                )
            )
            .from(clubMember)
            .where(filterByUserId(userId))
            .orderBy(clubMember.createAt.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        List<GetJoinClubResponse> results = query.fetch();

        results.stream()
            .map(j -> {
                String profileImage = S3Utils.getFullPath(j.clubProfileImage);
                j.setClubProfileImage(profileImage);
                return j;
            }).collect(Collectors.toList());

        long total = queryFactory.select(clubMember)
            .from(clubMember)
            .where(filterByUserId(userId))
            .stream()
            .count();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression filterByUserId(Long userId) {
        return clubMember.user.userId.eq(userId);
    }
}
