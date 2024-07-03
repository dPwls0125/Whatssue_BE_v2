package GDG.whatssue.domain.member.repository;
import GDG.whatssue.domain.club.dto.GetJoinClubResponse;
import GDG.whatssue.domain.club.entity.Club;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import GDG.whatssue.domain.member.entity.ClubMember;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    Optional<ClubMember> findByClub_IdAndUser_UserId(Long clubId, Long userId);
    Boolean existsByClub_IdAndUser_UserId(Long clubId, Long userId);
    Optional<List<ClubMember>> findByClubId(Long clubId);

    @Query("select count(m) from ClubMember m where m.club.id = :clubId")
    long countClubMember(@Param("clubId") Long clubId);

    @Query("select new GDG.whatssue.domain.club.dto.GetJoinClubResponse(" +
                        "c.id, c.clubName, i.storeFileName, m.createAt, m.role, " +
                            "(select count(subM) from ClubMember subM where subM.club = c))" +
            " from ClubMember m" +
                " join m.club c" +
                " join c.profileImage i" +
            " where m.user.userId = :userId" +
            " order by m.createAt"
    )
    Page<GetJoinClubResponse> getJoinClubList(@Param("userId") Long userId, Pageable pageable);

    @Query(
        "select m From ClubMember m" +
        " join fetch m.club c" +
        " join fetch c.profileImage ci" +
        " join fetch m.profileImage mi" +
        " where m.club.id = :clubId and m.user.userId = :userId"
    )
    ClubMember findMemberWithClub(@Param("clubId") Long clubId, @Param("userId") Long userId);
}