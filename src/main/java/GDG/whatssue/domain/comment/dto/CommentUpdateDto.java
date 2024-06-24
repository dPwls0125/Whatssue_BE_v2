package GDG.whatssue.domain.comment.dto;

import lombok.Data;

@Data
public class CommentUpdateDto {

    private Long commentId;

    private String content;

}
