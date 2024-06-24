package GDG.whatssue.domain.comment.service;

import GDG.whatssue.domain.comment.dto.ChildCommentAddDto;
import GDG.whatssue.domain.comment.dto.CommentAddDto;
import GDG.whatssue.domain.comment.dto.CommentDto;
import GDG.whatssue.domain.comment.dto.CommentUpdateDto;
import GDG.whatssue.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {

    void createComment(CommentAddDto commentAddDto, Long userId, Long clubId);

    void deleteComment( Long commentId, Long userId, Long clubId);

    void updateComment(CommentUpdateDto commentUpdateDto, Long userId, Long clubId);

    void createChildComment(ChildCommentAddDto childCommentAddDto, Long userId, Long clubId);

    Page<CommentDto> getCommentList(Long postId, int size, int page);

    void getMyCommentList(Long userId, Long clubId);


}
