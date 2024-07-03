package GDG.whatssue.domain.club.dto;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.entity.NamePolicy;
import GDG.whatssue.global.util.S3Utils;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    private String clubIntro;

    public GetClubInfoByPrivateCodeResponse(Club club, long clubMemberCount) {
        this.clubId = club.getId();
        this.clubProfileImage = S3Utils.getFullPath(club.getProfileImage().getStoreFileName());
        this.clubName = club.getClubName();
        this.namePolicy = club.getNamePolicy();
        this.createdAt = club.getCreateAt();
        this.clubIntro = club.getClubIntro();

        this.clubMemberCount = clubMemberCount;
    }
}
