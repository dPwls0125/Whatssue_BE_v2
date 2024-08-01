package GDG.whatssue.domain.clubjoinrequest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class JoinClubRequest {

    @NotNull(message = "가입하려는 모임 id는 필수 입력 값입니다.")
    private Long clubId;
}
