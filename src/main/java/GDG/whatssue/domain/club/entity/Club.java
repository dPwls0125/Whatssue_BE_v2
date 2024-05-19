package GDG.whatssue.domain.club.entity;


import GDG.whatssue.domain.club.dto.UpdateClubInfoRequest;
import GDG.whatssue.domain.file.entity.UploadFile;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.post.entity.Post;
import GDG.whatssue.domain.schedule.entity.Schedule;
import GDG.whatssue.global.common.BaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import java.util.UUID;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
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

    @Column(nullable = false)
    private String privateCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NamePolicy namePolicy;

    @OneToOne(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) //지연 로딩
    private UploadFile profileImage;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<ClubMember> clubMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "club")
    private List<ClubJoinRequest> clubJoinRequestList = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.REMOVE)
    private List<Schedule> scheduleList = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.REMOVE)
    private List<Post> postList = new ArrayList<>();

    //==연관관계 메서드==//
    public void addMember(ClubMember member) {
        clubMemberList.add(member);
        member.setClub(this);
    }

    public void setProfileImage(UploadFile profileImage) {
        this.profileImage = profileImage;
        profileImage.setClub(this); //연관관계 편의 메서드
    }

    //==생성메서드==//
    private Club(String clubName, String clubInfo, boolean isPrivate, String contactMeans, NamePolicy namePolicy) {
        this.clubName = clubName;
        this.clubIntro = clubInfo;
        this.isPrivate = isPrivate;
        this.contactMeans = contactMeans;
        this.namePolicy = namePolicy;

        this.createNewPrivateCode();
    }

    /**
     * 팩토리 메서드 패턴
     */
    public static Club createClub(String clubName, String clubInfo, boolean isPrivate, String contactMeans, NamePolicy namePolicy) {
        return new Club(clubName, clubInfo, isPrivate, contactMeans, namePolicy);
    }

    //==비즈니스 로직==//
    /**
     * 모임 정보 수정
     */
    public void updateClubInfo(UpdateClubInfoRequest requestDto) {
        this.clubName = requestDto.getClubName();
        this.clubIntro = requestDto.getClubIntro();
        this.contactMeans = requestDto.getContactMeans();
    }

    /**
     * 모임 가입신청 on / off
     */
    public void updateIsPrivate() {
        this.isPrivate = !this.isPrivate;
    }

    /**
     * 모임코드 갱신
     */
    public void createNewPrivateCode() {
        this.privateCode = UUID.randomUUID().toString().substring(0, 6);
    }

    /**
     * 가입 멤버 수 반환
     */
    public int getMemberCount() {
        return clubMemberList.size();
    }
}
