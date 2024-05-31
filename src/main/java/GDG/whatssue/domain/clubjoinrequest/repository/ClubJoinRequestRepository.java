package GDG.whatssue.domain.clubjoinrequest.repository;

import GDG.whatssue.domain.clubjoinrequest.entity.ClubJoinRequest;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubJoinRequestRepository extends JpaRepository<ClubJoinRequest, Long>, ClubJoinRequestRepositoryCustom{

    Optional<ClubJoinRequest> findByIdAndUser_UserId(Long joinRequestId, Long userId);
    Boolean existsByClub_IdAndUser_UserId(Long clubId, Long userId);
    List<ClubJoinRequest> findByClub_Id(Long clubId);
}
