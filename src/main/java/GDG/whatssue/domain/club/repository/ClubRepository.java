package GDG.whatssue.domain.club.repository;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.dto.GetClubInfoByPrivateCodeResponse;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubRepository extends JpaRepository<Club, Long> {

    @Query("select new GDG.whatssue.domain.club.dto.GetClubInfoByPrivateCodeResponse(" +
        "c.id, i.storeFileName, c.clubName, c.namePolicy, c.createAt, c.clubIntro, " +
                "(select count(m) from ClubMember m where m.club = c)" +
        ") " +
        "from Club c " +
            "join c.profileImage i " +
        "where c.privateCode = :privateCode")
    Optional<GetClubInfoByPrivateCodeResponse> findByPrivateCode(@Param("privateCode") String privateCode);
}
