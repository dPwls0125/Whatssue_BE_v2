package GDG.whatssue.domain.comment.repository;

import GDG.whatssue.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(Long id);

    Optional<List<Comment>> findByPostIdOrderByCreateAtDesc(Long postId);

    Optional<List<Comment>> findByPostId(Long postId);

    Page<Comment> findAllByPostIdAndParentCommentIsNullAndDeleteAtIsNull(Long postId, Pageable pageable);
    Page<Comment> findAllByPostIdAndDeleteAtIsNull(Long postId, Pageable pageable);
}
