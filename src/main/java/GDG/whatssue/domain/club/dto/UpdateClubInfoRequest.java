package GDG.whatssue.domain.club.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateClubInfoRequest {

    @Size(min = 2, max = 15, message = "모임 이름은 최소 2자, 최대 15자까지입니다.")
    String clubName;

    @Size(min = 10, max = 300, message = "모임 소개는 최소 10자, 최대 300자까지입니다.")
    String clubIntro;

    @NotNull(message = "대표 연락수단은 필수 입력값입니다.")
    String contactMeans;
}
