package GDG.whatssue.domain.member.repository;

import GDG.whatssue.domain.member.entity.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

}
