package GDG.whatssue.domain.clubjoinrequest.repository;

import GDG.whatssue.domain.clubjoinrequest.dto.GetJoinRequestsResponse;
import GDG.whatssue.domain.clubjoinrequest.entity.ClubJoinRequest;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubJoinRequestRepository extends JpaRepository<ClubJoinRequest, Long> {

    Optional<ClubJoinRequest> findByIdAndUser_UserId(Long joinRequestId, Long userId);
    Boolean existsByClub_IdAndUser_UserId(Long clubId, Long userId);
    List<ClubJoinRequest> findByClub_Id(Long clubId);

    @Query(value = "select new GDG.whatssue.domain.clubjoinrequest.dto.GetJoinRequestsResponse(r.id, c.id, c.clubName, r.status, r.updateAt) " +
            "from ClubJoinRequest r " +
            "left join r.user u " +
            "left join r.club c " +
            "where u.userId = :userId " +
            "order by r.createAt asc"
    )
    Page<GetJoinRequestsResponse> findAllJoinRequest(@Param("userId") Long userId, Pageable pageable);
}
