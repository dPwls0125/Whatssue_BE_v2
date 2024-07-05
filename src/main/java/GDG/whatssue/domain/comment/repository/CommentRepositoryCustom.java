package GDG.whatssue.domain.comment.repository;

import GDG.whatssue.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {
    Page<Comment> findFilteredParentComments(Long postId, Pageable pageable);
}
