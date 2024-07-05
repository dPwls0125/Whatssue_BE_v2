package GDG.whatssue.domain.club.repository;

import GDG.whatssue.domain.club.entity.Club;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubRepository extends JpaRepository<Club, Long> {
    @Query("select c from Club c" +
        " join fetch c.profileImage i" +
        " where c.privateCode = :privateCode")
    Optional<Club> findByPrivateCode(@Param("privateCode") String privateCode);
}