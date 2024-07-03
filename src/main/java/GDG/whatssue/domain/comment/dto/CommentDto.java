package GDG.whatssue.domain.comment.dto;


import GDG.whatssue.domain.comment.entity.Comment;
import GDG.whatssue.global.util.S3Utils;
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
    private LocalDateTime deleteAt;

    public static CommentDto of(Comment comment) {

        String storeFileName = comment.getClubMember().getProfileImage().getStoreFileName();

        return CommentDto.builder()
                .commentId(comment.getId())
                .writerId(comment.getClubMember().getId())
                .writerName(comment.getClubMember().getMemberName())
                .profileImage(S3Utils.getFullPath(storeFileName))
                .postId(comment.getPost().getId())
                .parentId(comment.getParentComment() == null ? null : comment.getParentComment().getId())
                .content(comment.getContent())
                .createdAt(comment.getCreateAt())
                .updateAt(comment.getUpdateAt())
                .deleteAt(comment.getDeleteAt())
                .build();

    }

    public static CommentDto nullOf(Comment comment) {
        return CommentDto.builder()
                .commentId(comment.getId())
                .writerId(null)
                .writerName(null)
                .profileImage(null)
                .postId(comment.getPost().getId())
                .parentId(comment.getParentComment().getId())
                .content(null)
                .createdAt(null)
                .updateAt(null)
                .deleteAt(null)
                .build();
    }

}
