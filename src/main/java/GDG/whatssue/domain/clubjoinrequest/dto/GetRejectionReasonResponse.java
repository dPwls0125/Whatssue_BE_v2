package GDG.whatssue.domain.clubjoinrequest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetRejectionReasonResponse {

    private Long clubJoinRequestId;
    private String rejectionReason;
}