package GDG.whatssue.domain.post.repository;


import static GDG.whatssue.domain.post.entity.QPost.*;

import GDG.whatssue.domain.post.entity.Post;
import GDG.whatssue.domain.post.entity.PostCategory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final JPAQueryFactory query;

    public Page<Post> findPosts(Long clubId, String keyword, LocalDateTime startDate, LocalDateTime endDate, String sortBy, PostCategory category, Pageable pageable) {
        List<Post> results = query
                .select(post)
                .from(post)
                .where(
                        filterClub(clubId),
                        containsKeyword(keyword),
                        betweenDates(startDate, endDate),
                        filterCategory(category)
                )
                .orderBy(sortBy.equals("Like") ? post.postLikeList.size().desc() : post.createAt.desc()) // 좋아요순 또는 최신순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query
                .select(post)
                .from(post)
                .where(
                        filterClub(clubId),
                        containsKeyword(keyword),
                        betweenDates(startDate, endDate)
                )
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression filterClub(Long clubId) {
        return post.club.id.eq(clubId);
    }

    private BooleanExpression containsKeyword(String keyword) {
        return keyword != null ? post.postTitle.containsIgnoreCase(keyword).or(post.postContent.containsIgnoreCase(keyword)) : null;
    }

    private BooleanExpression betweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return post.createAt.between(startDate, endDate);
    }

    private BooleanExpression filterCategory(PostCategory category) {
        return post.postCategory.eq(category);
    }
}
