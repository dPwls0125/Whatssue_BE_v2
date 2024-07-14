package GDG.whatssue.domain.club.entity;


import GDG.whatssue.domain.club.dto.UpdateClubInfoRequest;
import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.file.entity.ClubProfileImage;
import GDG.whatssue.global.common.BaseEntity;
import GDG.whatssue.global.error.CommonException;
import jakarta.persistence.*;

import java.util.UUID;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;

    @Column(nullable = false)
    private String clubName;

    @Column(nullable = false)
    private String clubIntro;

    @Column(nullable = false)
    private boolean isPrivate;

    @Column
    private String contactMeans;

    @Column
    private String link;

    @Column(nullable = false)
    private String privateCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NamePolicy namePolicy;

    @OneToOne(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true) //지연 로딩
    private ClubProfileImage profileImage;

    //==연관관계 메서드==//

    /**
     * 모임 프로필이미지 업데이트
     */
    public void updateProfileImage(ClubProfileImage profileImage) {
        if (this.profileImage != null) {
            this.profileImage.setClub(null);
        }

        this.profileImage = profileImage;
        profileImage.setClub(this);
    }

    //==생성메서드==//
    private Club(String clubName, String clubInfo, boolean isPrivate, String contactMeans, String link, NamePolicy namePolicy) {
        this.clubName = clubName;
        this.clubIntro = clubInfo;
        this.isPrivate = isPrivate;
        this.contactMeans = contactMeans;
        this.link = link;
        this.namePolicy = namePolicy;

        this.updatePrivateCode();
    }

    public static Club createClub(String clubName, String clubInfo, boolean isPrivate, String contactMeans, String link, NamePolicy namePolicy) {
        return new Club(clubName, clubInfo, isPrivate, contactMeans, link, namePolicy);
    }

    //==비즈니스 로직==//
    /**
     * 모임 정보 수정
     */
    public void updateClubInfo(UpdateClubInfoRequest requestDto) {
        this.clubName = requestDto.getClubName();
        this.clubIntro = requestDto.getClubIntro();
        this.contactMeans = requestDto.getContactMeans();
        this.link = requestDto.getLink();
    }

    /**
     * 모임 가입신청 on / off
     */
    public void updateIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    /**
     * 모임코드 갱신
     */
    public void updatePrivateCode() {
        this.privateCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    /**
     * 가입 신청 가능 검증
     */
    public void validateJoinable() {
        if (!isPrivate) {
            throw new CommonException(ClubErrorCode.EX3205);
        }
    }
}
