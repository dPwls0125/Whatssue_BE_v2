package GDG.whatssue.domain.club.dto;

import GDG.whatssue.domain.member.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetJoinClubListResponse {

    public Long clubId;
    public String clubName;
    public String clubProfileImage;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime createdAt;
    public Role role;

    //현재 인원 TODO

    @Builder
    public GetJoinClubListResponse(Long clubId, String clubName, String clubProfileImage,
        LocalDateTime createdAt, Role role) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.clubProfileImage = clubProfileImage;
        this.createdAt = createdAt;
        this.role = role;
    }
}
