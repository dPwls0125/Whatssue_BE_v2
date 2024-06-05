package GDG.whatssue.domain.member.repository;

import GDG.whatssue.domain.clubjoinrequest.dto.GetJoinClubResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClubMemberRepositoryCustom {
    Page<GetJoinClubResponse> getJoinClubList(Long userId, Pageable pageable);
}
