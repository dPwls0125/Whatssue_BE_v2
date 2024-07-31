package GDG.whatssue.domain.comment.dto;

import GDG.whatssue.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MyCommentDto {

    private Long commentId;
    private Long postId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long parentId;


    public static MyCommentDto of(Comment comment){
        return MyCommentDto.builder()
                .commentId(comment.getId())
                .postId(comment.getPost().getId())
                .createdAt(comment.getCreateAt())
                .updatedAt(comment.getUpdateAt())
                .content(comment.getContent())
                .parentId(comment.getParentComment() == null ? null : comment.getParentComment().getId())
                .build();
    }



}