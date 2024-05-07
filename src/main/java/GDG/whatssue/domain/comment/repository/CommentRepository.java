package GDG.whatssue.domain.comment.repository;

import GDG.whatssue.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Override
    Optional<Comment> findById(Long id);
    Optional<List<Comment>> findByPostId(Long postId);
}
