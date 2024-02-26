package GDG.whatssue.domain.club.repository;

import GDG.whatssue.domain.club.entity.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

}
