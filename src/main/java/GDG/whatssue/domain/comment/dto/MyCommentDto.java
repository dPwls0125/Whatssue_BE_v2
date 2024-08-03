package GDG.whatssue.domain.comment.dto;

import GDG.whatssue.domain.comment.entity.Comment;
import GDG.whatssue.domain.post.dto.GetPostResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MyCommentDto {

    private CommentDto comment;
    private GetPostResponse post;

    public static MyCommentDto of(Comment comment, GetPostResponse post){
        return MyCommentDto.builder()
                .comment(CommentDto.of(comment))
                .post(post)
                .build();
    }



}