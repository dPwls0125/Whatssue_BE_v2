package GDG.whatssue.domain.club.dto;

import GDG.whatssue.domain.member.entity.Role;
import GDG.whatssue.global.util.S3Utils;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetJoinClubResponse {

    public Long clubId;
    public String clubName;
    public String clubProfileImage;
    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDateTime createdAt;
    public Role role;
    public long memberCount;

    public void setClubProfileImage(String clubProfileImage) {
        this.clubProfileImage = clubProfileImage;
    }

    public GetJoinClubResponse(Long clubId, String clubName, String clubProfileImage,
        LocalDateTime createdAt, Role role, long memberCount) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.clubProfileImage = S3Utils.getFullPath(clubProfileImage);
        this.createdAt = createdAt;
        this.role = role;
        this.memberCount = memberCount;
    }
}
