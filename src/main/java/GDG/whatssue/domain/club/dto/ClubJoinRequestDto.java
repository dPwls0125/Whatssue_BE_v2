package GDG.whatssue.domain.club.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubJoinRequestDto {

    @NotBlank(message = "가입코드는 필수 입력값입니다.")
    @Size(min = 6, max = 6, message = "클럽 가입코드는 6자리입니다")
    private String privateCode;
}
