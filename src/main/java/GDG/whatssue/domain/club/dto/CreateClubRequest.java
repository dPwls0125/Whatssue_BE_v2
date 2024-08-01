package GDG.whatssue.domain.club.dto;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.entity.NamePolicy;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

@Getter
public class CreateClubRequest {

    @Size(min = 2, max = 15, message = "모임 이름은 최소 2자, 최대 15자까지입니다.")
    private String clubName; //클럽 이름(필수)

    @Size(min = 10, max = 300, message = "모임 소개는 최소 10자, 최대 300자까지입니다.")
    private String clubIntro; //클럽 소개(필수)

    @NotNull(message = "모임 비공개 여부는 필수 선택 사항입니다.")
    private Boolean isPrivate; //비공개 여부(필수)

    @NotNull(message = "대표 연락수단은 필수 입력값입니다.")
    private String contactMeans; //연락수단

    @NotNull(message = "모임 이름정책은 필수 선택 사항입니다.")
    private NamePolicy namePolicy;

    private String link;

    public Club toEntity() {
        return Club.createClub(clubName, clubIntro, isPrivate, contactMeans, link, namePolicy);
    }
}
