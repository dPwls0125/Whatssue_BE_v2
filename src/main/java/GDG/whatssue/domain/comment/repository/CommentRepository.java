package GDG.whatssue.domain.comment.repository;

import GDG.whatssue.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    Optional<Comment> findById(Long id);

    Page<Comment> findByParentComment_Id(Long parentId, Pageable pageable);
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.parentComment.id = :parentId")
    long countByParentCommentId(@Param("parentId") Long parentId);

    Page<Comment> findAllByPostIdAndParentCommentIsNull(Long postId, Pageable pageable);
}
