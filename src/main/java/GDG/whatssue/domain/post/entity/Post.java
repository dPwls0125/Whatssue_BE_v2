package GDG.whatssue.domain.post.entity;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.comment.entity.Comment;
import GDG.whatssue.domain.file.entity.UploadFile;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.post.exception.PostErrorCode;
import GDG.whatssue.global.common.BaseEntity;
import GDG.whatssue.global.error.CommonException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_member_id", nullable = false)
    private ClubMember writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Column(nullable = false)
    private String postTitle;

    @Column(nullable = false)
    private String postContent;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<UploadFile> postImageFiles = new ArrayList<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostCategory postCategory;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostLike> postLikeList = new ArrayList<>();
    
    //연관관계 메서드
    public void addPostImageFile(UploadFile uploadFile) {
        uploadFile.setPost(this);
        this.postImageFiles.add(uploadFile);
    }
    public void clearPostImageFiles() {
        if (this.postImageFiles != null) {
            this.postImageFiles.clear();
        }
    }

    //==생성 메서드==//
    private Post(Club club, ClubMember writer, String postTitle, String postContent, PostCategory postCategory) {
        this.club = club;
        this.writer = writer;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postCategory = postCategory;
    }

    public static Post createPost(Club club, ClubMember writer, String postTitle, String postContent, PostCategory postCategory) {

        if (postCategory == PostCategory.NOTICE && !writer.checkManagerRole()){
            throw new CommonException(PostErrorCode.EX7200);//관리자만 작성 가능
        }

        return new Post(club, writer, postTitle, postContent, postCategory);

    }
    //업데이트 메소드
    public void updatePost(String postTitle, String postContent) {

        if (this.postCategory == PostCategory.NOTICE && !writer.checkManagerRole()) {
            throw new CommonException(PostErrorCode.EX7203);//관리자만 업데이트 가능
        }
        this.postTitle = postTitle;
        this.postContent = postContent;
    }
}
