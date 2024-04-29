package GDG.whatssue.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CommentRequestDto {

    private Long parentId;
    private Long postId;
    private String content;


}
