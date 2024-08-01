package GDG.whatssue.domain.comment.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentAddDto {

    private Long postId;
    private String content;

    public CommentAddDto(Long postId, String content) {
        this.postId = postId;
        this.content = content;
    }
}
