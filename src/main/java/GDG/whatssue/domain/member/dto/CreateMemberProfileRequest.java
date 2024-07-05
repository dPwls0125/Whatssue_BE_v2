package GDG.whatssue.domain.member.dto;

import GDG.whatssue.domain.member.entity.ClubMember;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateMemberProfileRequest {

    @Size(min = 1, max = 15, message = "멤버 이름은 최소 1자, 최대 15자까지 입니다.")
    private String memberName;

    @Size(min = 1, max = 30, message = "")
    private String memberIntro;

    @NotNull(message = "이메일 공개 여부는 필수 선택 사항입니다.")
    private Boolean isEmailPublic;

    @NotNull(message = "전화번호 공개 여부는 필수 선택 사항입니다.")
    private Boolean isPhonePublic;


}
