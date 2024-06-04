package GDG.whatssue.domain.clubjoinrequest.entity;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.global.common.BaseEntity;
import GDG.whatssue.global.error.CommonException;
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

    private String rejectionReason;

    @Enumerated(EnumType.STRING)
    private ClubJoinRequestStatus status;

    //==생성메서드==//
    private ClubJoinRequest(Club club, User user) {
        this.club = club;
        this.user = user;
        this.rejectionReason = null;
        this.status = ClubJoinRequestStatus.WAITING;
    }

    public static ClubJoinRequest createClubJoinRequest(Club club, User user) {
        return new ClubJoinRequest(club, user);
    }

    //==비즈니스 로직==//
    public String fetchRejectionReason() {
        if (status != ClubJoinRequestStatus.REJECTED) {
            throw new CommonException(ClubErrorCode.EX3202);
        }

        return rejectionReason;
    }

    public void cancel() {
        if (status != ClubJoinRequestStatus.WAITING) {
            throw new CommonException(ClubErrorCode.EX3203);
        }

        status = ClubJoinRequestStatus.CANCELED;
    }

    public void validateDeletable() {
        if (status == ClubJoinRequestStatus.WAITING) {
            throw new CommonException(ClubErrorCode.EX3204);
        }
    }
}
