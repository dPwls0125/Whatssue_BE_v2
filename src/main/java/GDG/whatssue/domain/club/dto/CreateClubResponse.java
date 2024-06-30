package GDG.whatssue.domain.club.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateClubResponse {

    Long clubId;

    @Builder
    public CreateClubResponse(Long clubId) {
        this.clubId = clubId;
    }
}
