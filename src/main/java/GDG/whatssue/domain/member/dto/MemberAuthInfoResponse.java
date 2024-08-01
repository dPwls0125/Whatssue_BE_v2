package GDG.whatssue.domain.member.dto;

import GDG.whatssue.domain.club.entity.NamePolicy;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.entity.Role;
import GDG.whatssue.global.util.S3Utils;
import lombok.Getter;

@Getter
public class MemberAuthInfoResponse {

    private Long memberId;
    private String memberName;
    private String memberProfileImage;
    private Role role;
    private String clubProfileImage;
    private String clubName;
    private NamePolicy namePolicy;

    public MemberAuthInfoResponse(ClubMember member) {
        this.memberId = member.getId();
        this.memberName = member.getMemberName();
        this.memberProfileImage = S3Utils.getFullPath(member.getProfileImage().getStoreFileName());
        this.role = member.getRole();

        this.clubProfileImage = S3Utils.getFullPath(member.getClub().getProfileImage().getStoreFileName());
        this.clubName = member.getClub().getClubName();
        this.namePolicy = member.getClub().getNamePolicy();
    }
}
