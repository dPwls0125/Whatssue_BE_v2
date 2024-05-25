package GDG.whatssue.domain.member.entity;

import GDG.whatssue.domain.attendance.entity.MemberAttendanceResult;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.file.entity.UploadFile;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest;
import GDG.whatssue.domain.post.entity.Post;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.global.common.BaseEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

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

    @OneToOne(mappedBy = "clubMember", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY) //지연 로딩 설정
    private UploadFile profileImage;

    @OneToOne(mappedBy = "clubMember", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY) //지연 로딩 설정
    private MemberAttendanceResult memberAttendanceResult;

    @OneToMany(mappedBy = "clubMember", cascade = CascadeType.REMOVE)
    private List<OfficialAbsenceRequest> OfficialAbsenceRequestList = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<Post> postList = new ArrayList<>();

    //==연관관계 메서드==//
    private void setUser(User user) {
        this.user = user;
        user.getClubMemberList().add(this);
    }

    public void setProfileImage(UploadFile profileImage) {
        this.profileImage = profileImage;
        profileImage.setClubMember(this);
    }

    //==생성메서드==//
    private ClubMember(User user) {
        setUser(user);

        this.role = Role.MEMBER;
        this.memberName = user.getUserName();
        this.isPhonePublic = false;
        this.isEmailPublic = false;
        this.isFirstVisit = true;
    }

    public static ClubMember newMember(User user) {
        return new ClubMember(user);
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

    public void setClub(Club club) {
        this.club = club;
    }
}
