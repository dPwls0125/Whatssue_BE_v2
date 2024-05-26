package GDG.whatssue.domain.member.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


import GDG.whatssue.domain.member.entity.ClubMember;

import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long>, ClubMemberRepositoryCustom {
    Optional<ClubMember> findByClub_IdAndUser_UserId(Long clubId, Long userId);
    List<ClubMember> findByUser_UserId(Long userId);
    Boolean existsByClub_IdAndUser_UserId(Long clubId, Long userId);
    Optional<List<ClubMember>> findByClubId(Long clubId);
}