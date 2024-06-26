package GDG.whatssue.domain.club.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateClubPrivateRequest {

    @NotNull
    private Boolean isPrivate;
}
