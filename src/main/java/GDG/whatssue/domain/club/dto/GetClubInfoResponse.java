package GDG.whatssue.domain.club.dto;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.entity.NamePolicy;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetClubInfoResponse {

    private String clubName;
    private String clubIntro;
    private Boolean isPrivate;
    private String contactMeans;
    private String link;
    private NamePolicy namePolicy;
    private String privateCode;
    private String clubProfileImage;
    private long memberCount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDateTime createdAt;

    public GetClubInfoResponse(Club club, String clubProfileImage, Long memberCount) {
        this.clubName = club.getClubName();
        this.clubIntro = club.getClubIntro();
        this.isPrivate = club.isPrivate();
        this.contactMeans = club.getContactMeans();
        this.link = club.getLink();
        this.namePolicy = club.getNamePolicy();
        this.privateCode = club.getPrivateCode();
        this.clubProfileImage = clubProfileImage;
        this.memberCount = memberCount;
        this.createdAt = club.getCreateAt();
    }
}
