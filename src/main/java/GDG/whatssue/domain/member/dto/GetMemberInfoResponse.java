package GDG.whatssue.domain.member.dto;

import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.entity.Role;
import GDG.whatssue.global.util.S3Utils;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GetMemberInfoResponse {

    private String memberName;
    private String memberProfileImage;
    private Role role;
    private String memberIntro;
    private String clubName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    public GetMemberInfoResponse(ClubMember member) {
        this.memberName = member.getMemberName();
        this.memberProfileImage = S3Utils.getFullPath(member.getProfileImage().getStoreFileName());
        this.role = member.getRole();
        this.memberIntro = member.getMemberIntro();
        this.clubName = member.getClub().getClubName();
        this.createdAt = member.getCreateAt();
    }
}
