package GDG.whatssue.domain.post.entity;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.comment.entity.Comment;
import GDG.whatssue.domain.file.entity.UploadFile;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.global.common.BaseEntity;
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
import lombok.Builder;
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<UploadFile> postImageFiles = new ArrayList<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostCategory postCategory;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();
    
    //연관관계 메서드
    public void addPostImageFile(UploadFile uploadFile) {
        this.postImageFiles.add(uploadFile);
        uploadFile.setPost(this); //연관관계 편의 메서드
    }

    @Builder
    public Post(Long id, ClubMember writer, Club club, String postTitle, String postContent,
        PostCategory postCategory) {
        this.id = id;
        this.writer = writer;
        this.club = club;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postCategory = postCategory;
    }


}
