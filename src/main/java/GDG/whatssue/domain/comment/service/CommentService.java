package GDG.whatssue.domain.comment.service;

import GDG.whatssue.domain.comment.dto.*;
import GDG.whatssue.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {

    void createComment(CommentAddDto commentAddDto, Long userId, Long clubId);

    void deleteComment( Long commentId, Long userId, Long clubId);

    void updateComment(CommentUpdateDto commentUpdateDto, Long userId, Long clubId);

    void createChildComment(ChildCommentAddDto childCommentAddDto, Long userId, Long clubId);

    Page<CommentDto> getParentCommentList(Long postId, int size, int page);

    Page<CommentDto> getChildCommentList(Long parentId, int size, int page);

    MyCommentListResponse getMyCommentList(Long userId, Long clubId, int size, int page);


}
