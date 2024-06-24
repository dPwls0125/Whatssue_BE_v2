package GDG.whatssue.domain.comment.service;

import GDG.whatssue.domain.comment.dto.ChildCommentAddDto;
import GDG.whatssue.domain.comment.dto.CommentAddDto;
import GDG.whatssue.domain.comment.entity.Comment;

import java.util.List;

public interface CommentService {

    void createComment(CommentAddDto commentAddDto, Long userId, Long clubId);

    void deleteComment( Long commentId, Long userId, Long clubId);

    void updateComment( Long commentId, String content);

    void createChildComment(ChildCommentAddDto childCommentAddDto, Long userId, Long clubId);

    List<Comment> getCommentList(Long postId);



    void getMyCommentList(Long userId, Long clubId);


}
