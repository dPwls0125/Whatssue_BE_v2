package GDG.whatssue.domain.clubjoinrequest.entity;

import static GDG.whatssue.domain.clubjoinrequest.entity.ClubJoinRequestStatus.*;

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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
        this.status = WAITING;
    }

    public static ClubJoinRequest createClubJoinRequest(Club club, User user) {
        return new ClubJoinRequest(club, user);
    }

    //==비즈니스 로직==//

    /**
     * 가입신청 취소
     */
    public void cancel() {
        if (status != WAITING) { //처리가 완료되면 취소 불가
            throw new CommonException(ClubErrorCode.EX3203);
        }

        status = CANCELED;
    }

    /**
     * 가입신청 내역이 삭제될 수 있는지 검증
     */
    public void validateDeletable() {
        if (status == WAITING) { //대기중이면 내역 삭제불가
            throw new CommonException(ClubErrorCode.EX3204);
        }
    }

    /**
     * 가입신청 수락 TODO
     */

    /**
     * 가입신청 거절 TODO
     */

    //==조회 로직==//
    /**
     * 거절 사유 조회
     */
    public String fetchRejectionReason() {
        if (status != REJECTED) { //거절된 신청이 아니면
            throw new CommonException(ClubErrorCode.EX3202);
        }

        return rejectionReason;
    }
}
