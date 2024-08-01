package GDG.whatssue.domain.comment.repository;

import GDG.whatssue.domain.comment.dto.MyCommentDto;
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

    Page<Comment> findByParentComment_IdAndDeleteAtIsNull(Long parentId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.clubMember.id = :memberId AND c.deleteAt IS NULL")
    Page<Comment> findMyCommentList(Long memberId, Pageable pageable);

}
