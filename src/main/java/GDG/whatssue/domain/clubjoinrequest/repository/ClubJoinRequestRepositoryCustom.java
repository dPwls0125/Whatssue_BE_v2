package GDG.whatssue.domain.clubjoinrequest.repository;

import GDG.whatssue.domain.clubjoinrequest.dto.GetJoinRequestsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClubJoinRequestRepositoryCustom {
    Page<GetJoinRequestsResponse> findAllJoinRequest(Long userId, Pageable pageable);
}
