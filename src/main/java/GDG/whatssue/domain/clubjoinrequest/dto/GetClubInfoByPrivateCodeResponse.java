package GDG.whatssue.domain.clubjoinrequest.dto;

import GDG.whatssue.domain.club.entity.NamePolicy;
import GDG.whatssue.global.util.S3Utils;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    private String clubIntro;

    public GetClubInfoByPrivateCodeResponse(Long clubId, String storeFileName, String clubName,
        NamePolicy namePolicy, LocalDateTime createdAt, String clubIntro, long clubMemberCount) {
        this.clubId = clubId;
        this.clubProfileImage = S3Utils.getFullPath(storeFileName);
        this.clubName = clubName;
        this.clubMemberCount = clubMemberCount;
        this.namePolicy = namePolicy;
        this.createdAt = createdAt;
        this.clubIntro = clubIntro;
    }
}
