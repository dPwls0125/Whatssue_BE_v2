package GDG.whatssue.domain.post.dto;

import GDG.whatssue.domain.post.entity.PostCategory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetPostResponse {

    private Long postId;
    private String writerProfileImage;
    private String writerName;
    private String postTitle;
    private String postContent;
    private Map<Integer, String> uploadImage;
    private PostCategory postCategory;
    private Long postLikeCount;
    private Long commentCount;
    private Boolean isLiked;
    private LocalDateTime createdAt;
    @Builder
    public GetPostResponse(Long postId, String writerProfileImage, String writerName,
        String postTitle,
        String postContent, Map<Integer, String> uploadImage, PostCategory postCategory, Long postLikeCount, Long commentCount, Boolean isLiked, LocalDateTime createdAt) {
        this.postId = postId;
        this.writerProfileImage = writerProfileImage;
        this.writerName = writerName;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.uploadImage = uploadImage;
        this.postCategory = postCategory;
        this.postLikeCount = postLikeCount;
        this.commentCount = commentCount;
        this.isLiked = isLiked;
        this.createdAt = createdAt;
    }

}
