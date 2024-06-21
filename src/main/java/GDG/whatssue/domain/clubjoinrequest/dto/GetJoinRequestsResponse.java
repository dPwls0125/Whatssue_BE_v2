package GDG.whatssue.domain.clubjoinrequest.dto;

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

    public GetJoinRequestsResponse(Long clubJoinRequestId, Long clubId, String clubName,
        ClubJoinRequestStatus status, LocalDateTime updatedAt) {
        this.clubJoinRequestId = clubJoinRequestId;
        this.clubId = clubId;
        this.clubName = clubName;
        this.status = status;
        this.updatedAt = updatedAt;
    }
}
