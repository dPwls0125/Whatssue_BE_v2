package GDG.whatssue.domain.comment.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CommentDto {

    private String writer;
    private String memberId;
    private String content;
    private String createdAt;
    private Long parentId;
    private Long postId;


}
