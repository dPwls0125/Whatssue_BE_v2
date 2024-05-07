package GDG.whatssue.domain.comment.dto;

import lombok.*;
@Getter
@Setter
public class CommentResponseDto extends CommentBaseDto {
    private String createdAt;
    private String writerName;
    private Long parentId;

    public CommentResponseDto(Long commentId, String content, String createdAt, String writerName, Long parentId) {
        super(commentId, content);
        this.createdAt = createdAt;
        this.writerName = writerName;
        this.parentId = parentId;
    }
}
