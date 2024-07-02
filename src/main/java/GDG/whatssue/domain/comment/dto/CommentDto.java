package GDG.whatssue.domain.comment.dto;


import GDG.whatssue.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class CommentDto {

    private Long commentId;
    private Long writerId;
    private String writerName;
    private Long postId;
    private Long parentId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private String profileImage;

    public static CommentDto of(Comment comment, String profileImage) {

        return CommentDto.builder()
                .commentId(comment.getId())
                .writerId(comment.getClubMember().getId())
                .writerName(comment.getClubMember().getMemberName())
                .profileImage(profileImage)
                .postId(comment.getPost().getId())
                .parentId(comment.getParentComment() == null ? null : comment.getParentComment().getId())
                .content(comment.getContent())
                .createdAt(comment.getCreateAt())
                .updateAt(comment.getUpdateAt())
                .build();

    }

}
