package GDG.whatssue.domain.comment.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class CommentCreateDto extends CommentBaseDto {
    private Long parentId;
    private Long postId;
}
