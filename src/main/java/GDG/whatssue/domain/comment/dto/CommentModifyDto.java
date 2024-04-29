package GDG.whatssue.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentModifyDto extends CommentBaseDto{
    private Long writer_memberId;
}
