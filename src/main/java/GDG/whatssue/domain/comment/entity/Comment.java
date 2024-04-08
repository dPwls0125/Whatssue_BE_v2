package GDG.whatssue.domain.comment.entity;

import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.post.entity.Post;
import GDG.whatssue.global.common.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id",unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "club_member_id", nullable = false)
    private ClubMember clubMember;

    @Column(name = "comment_content",nullable = false)
    private String content;

}