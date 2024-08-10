package GDG.whatssue.domain.member.entity;

import static GDG.whatssue.domain.member.exception.ClubMemberErrorCode.EX2200;

import GDG.whatssue.domain.attendance.entity.MemberAttendanceResult;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.file.entity.MemberProfileImage;
import GDG.whatssue.domain.member.dto.CreateMemberProfileRequest;
import GDG.whatssue.domain.member.dto.ModifyMemberProfileRequest;
import GDG.whatssue.domain.post.entity.Post;
import GDG.whatssue.domain.schedule.entity.Schedule;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.global.common.BaseEntity;
import GDG.whatssue.global.error.CommonException;
import jakarta.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class ClubMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //지연 로딩 설정
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY) //지연 로딩 설정
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private String memberIntro;

    @Column
    private String memberName;

    @Column
    private boolean isEmailPublic;

    @Column
    private boolean isPhonePublic;

    @Column(nullable = false)
    private boolean isFirstVisit;

    @OneToOne(mappedBy = "clubMember", cascade = {CascadeType.REMOVE, CascadeType.ALL}, fetch = FetchType.LAZY) //지연 로딩 설정
    private MemberProfileImage profileImage;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "register", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> Schedules = new ArrayList<>();

    @OneToOne(mappedBy = "clubMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberAttendanceResult memberAttendanceResults;

    //==연관관계 메서드==//
    public void changeProfileImage(MemberProfileImage profileImage) {
        profileImage.setClubMember(this);
        this.profileImage = profileImage;
    }

    //==생성메서드==//
    private ClubMember(Club club, User user) {
        this.user = user;
        this.club = club;
        this.role = Role.MEMBER;
        this.memberName = user.getUserName();
        this.isPhonePublic = false;
        this.isEmailPublic = false;
        this.isFirstVisit = true;
    }

    public static ClubMember newMember(Club club, User user) {
        return new ClubMember(club, user);
    }

    //==비즈니스 로직==//

    /**
     * 멤버 프로필 설정
     */
    public void updateProfile(String memberIntro, String memberName, boolean isEmailPublic, boolean isPhonePublic) {
        this.memberIntro = memberIntro;
        this.memberName = memberName;
        this.isEmailPublic = isEmailPublic;
        this.isPhonePublic = isPhonePublic;
    }

    public void updateProfile(ModifyMemberProfileRequest request, String memberName){
        this.memberIntro = request.getMemberIntro();
        this.memberName = memberName;
        this.isEmailPublic = request.getIsEmailPublic();
        this.isPhonePublic = request.getIsPhonePublic();
    }

    public void updateProfile(CreateMemberProfileRequest request, String memberName){
        this.memberIntro = request.getMemberIntro();
        this.memberName = memberName;
        this.isEmailPublic = request.getIsEmailPublic();
        this.isPhonePublic = request.getIsPhonePublic();
    }

    public void setFirstVisitFalse(){
        this.isFirstVisit = false;
    }

    /**
     * 관리자 권한 부여
     */
    public void switchToManager() {
        this.role = Role.MANAGER;
    }

    /**
     * 멤버 권한 부여
     */
    public void switchToMember() {
        this.role = Role.MEMBER;
    }

    public boolean checkManagerRole() {
        return role == Role.MANAGER;
    }

    public void validateFirstVisit() {
        if (this.isFirstVisit == true) {
            throw new CommonException(EX2200);
        }
    }
}
