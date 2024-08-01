package GDG.whatssue.domain.clubjoinrequest.dto;

import GDG.whatssue.domain.clubjoinrequest.entity.ClubJoinRequest;
import GDG.whatssue.domain.clubjoinrequest.entity.ClubJoinRequestStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetJoinRequestsResponse {

    private Long clubJoinRequestId;
    private Long clubId;
    private String clubName;
    private ClubJoinRequestStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updatedAt;

    public GetJoinRequestsResponse(ClubJoinRequest request) {
        this.clubJoinRequestId = request.getId();
        this.clubId = request.getClub().getId();
        this.clubName = request.getClub().getClubName();
        this.status = request.getStatus();
        this.updatedAt = request.getUpdateAt();
    }
}
