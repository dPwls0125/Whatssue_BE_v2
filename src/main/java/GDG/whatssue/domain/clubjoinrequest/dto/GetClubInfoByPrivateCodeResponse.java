package GDG.whatssue.domain.clubjoinrequest.dto;

import GDG.whatssue.domain.club.entity.NamePolicy;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetClubInfoByPrivateCodeResponse {

    private Long clubId;
    private String clubProfileImage;
    private String clubName;
    private long clubMemberCount;
    private NamePolicy namePolicy;
    private LocalDate createdAt;
    private String clubIntro;

    @Builder
    public GetClubInfoByPrivateCodeResponse(Long clubId, String clubProfileImage, String clubName,
        long clubMemberCount, NamePolicy namePolicy, LocalDate createdAt, String clubIntro) {
        this.clubId = clubId;
        this.clubProfileImage = clubProfileImage;
        this.clubName = clubName;
        this.clubMemberCount = clubMemberCount;
        this.namePolicy = namePolicy;
        this.createdAt = createdAt;
        this.clubIntro = clubIntro;
    }
}
