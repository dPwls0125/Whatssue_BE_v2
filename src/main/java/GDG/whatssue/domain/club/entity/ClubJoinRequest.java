package GDG.whatssue.domain.club.entity;

import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class ClubJoinRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_join_request_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //==생성메서드==//
    private ClubJoinRequest(Club club, User user) {
        this.club = club;
        club.getClubJoinRequestList().add(this); //연관관계 편의 메서드

        this.user = user;
        user.getClubJoinRequestList().add(this); //연관관계 편의 메서드
    }

    public static ClubJoinRequest of(Club club, User user) {
        return new ClubJoinRequest(club, user);
    }
}
