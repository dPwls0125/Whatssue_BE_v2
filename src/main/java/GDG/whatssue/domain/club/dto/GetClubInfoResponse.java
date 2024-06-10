package GDG.whatssue.domain.club.dto;

import GDG.whatssue.domain.club.entity.NamePolicy;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetClubInfoResponse {

    private String clubName;
    private String clubIntro;
    private Boolean isPrivate;
    private String contactMeans;
    private NamePolicy namePolicy;
    private String privateCode;
    private String clubProfileImage;
    private long memberCount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDateTime createdAt;

    @Builder
    public GetClubInfoResponse(String clubName, String clubIntro, Boolean isPrivate,
        String contactMeans, NamePolicy namePolicy, String privateCode, String clubProfileImage, long memberCount, LocalDateTime createdAt) {
        this.clubName = clubName;
        this.clubIntro = clubIntro;
        this.isPrivate = isPrivate;
        this.contactMeans = contactMeans;
        this.namePolicy = namePolicy;
        this.privateCode = privateCode;
        this.clubProfileImage = clubProfileImage;
        this.memberCount = memberCount;
        this.createdAt = createdAt;
    }
}
