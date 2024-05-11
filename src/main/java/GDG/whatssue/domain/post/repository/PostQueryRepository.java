package GDG.whatssue.domain.post.repository;


import static GDG.whatssue.domain.post.entity.QPost.*;

import GDG.whatssue.domain.post.entity.Post;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final JPAQueryFactory query;

    //카테고리가 where로 들어가야하나?
    public List<Post> findPosts(Long clubId, String searchQuery, String startDate, String endDate) {
        return query.
            select(post)
            .from(post)
            .where(filterClub(clubId))
            .fetch();
    }

    private BooleanExpression filterClub(Long clubId) {
        return post.club.id.eq(clubId);
    }

}
