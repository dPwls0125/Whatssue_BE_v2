package GDG.whatssue.domain.comment.repository;


import GDG.whatssue.domain.comment.entity.Comment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Comment> findFilteredParentComments(Long postId, Pageable pageable) {
        String queryStr = "SELECT c FROM Comment c " +
                "WHERE c.post.id = :postId " +
                "AND c.parentComment IS NULL " +
                "AND (c.deleteAt IS NULL OR " +
                "(c.deleteAt IS NOT NULL AND (SELECT COUNT(sc) FROM Comment sc WHERE sc.parentComment.id = c.id AND sc.deleteAt IS NULL) > 0))";

        TypedQuery<Comment> query = entityManager.createQuery(queryStr, Comment.class)
                .setParameter("postId", postId);
        System.out.print("query.getResultList(): ");
        query.getResultList().forEach(comment -> System.out.println(comment.getId()));

        int totalRows = query.getResultList().size();

        List<Comment> comments = query.setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        System.out.print("comments: ");
        comments.forEach(comment -> System.out.println(comment.getId()));

        return new PageImpl<>(comments, pageable, totalRows);
    }
}
