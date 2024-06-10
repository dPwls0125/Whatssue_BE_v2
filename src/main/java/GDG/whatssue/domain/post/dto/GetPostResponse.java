package GDG.whatssue.domain.post.dto;

import GDG.whatssue.domain.post.entity.PostCategory;
import java.util.List;
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
    private List<String> uploadImage;
    private PostCategory postCategory;
    private Long postLikeCount;
    private Boolean isLiked;

    @Builder
    public GetPostResponse(Long postId, String writerProfileImage, String writerName,
        String postTitle,
        String postContent, List<String> uploadImage, PostCategory postCategory, Long postLikeCount, Boolean isLiked) {
        this.postId = postId;
        this.writerProfileImage = writerProfileImage;
        this.writerName = writerName;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.uploadImage = uploadImage;
        this.postCategory = postCategory;
        this.postLikeCount = postLikeCount;
        this.isLiked = isLiked;
    }
}
