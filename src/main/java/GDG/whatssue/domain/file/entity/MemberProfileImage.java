package GDG.whatssue.domain.file.entity;

import GDG.whatssue.domain.member.entity.ClubMember;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfileImage extends UploadFile{
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_member_id")
    private ClubMember clubMember;

    public void setClubMember(ClubMember clubMember) {
        this.clubMember = clubMember;
    }

    //==생성 메서드==//
    private MemberProfileImage(String uploadFileName, String storeFileName) {
        super(uploadFileName, storeFileName);
    }

    public static MemberProfileImage of(String uploadFileName, String storeFileName) {
        return new MemberProfileImage(uploadFileName, storeFileName);
    }
}
