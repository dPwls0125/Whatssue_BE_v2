package GDG.whatssue.domain.comment.entity;

import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.post.entity.Post;
import GDG.whatssue.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id",unique = true)
    private Long id;

    @ManyToOne
    @Column(name = "parent_id")
    private Comment parentComment; // 부모 댓글

    @OneToMany
    @JoinColumn(name = "parent_id")
    private List<Comment> childComments; // 자식 댓글 리스트

    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne
    private Post post;

    @ManyToOne
    @JoinColumn(name = "club_member_id", nullable = false)
    private ClubMember clubMember;

    @Column(name = "comment_content",nullable = false)
    private String content;


}