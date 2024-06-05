package GDG.whatssue.domain.clubjoinrequest.dto;

import GDG.whatssue.domain.member.entity.Role;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime createdAt;
    public Role role;
    public long memberCount;

    public void setClubProfileImage(String clubProfileImage) {
        this.clubProfileImage = clubProfileImage;
    }

    @Builder
    public GetJoinClubResponse(Long clubId, String clubName, String clubProfileImage,
        LocalDateTime createdAt, Role role, long memberCount) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.clubProfileImage = clubProfileImage;
        this.createdAt = createdAt;
        this.role = role;
        this.memberCount = memberCount;
    }
}
