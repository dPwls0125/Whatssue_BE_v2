package GDG.whatssue.domain.post.repository;

import GDG.whatssue.domain.post.entity.PostLike;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostAndClubMember(Post post, ClubMember clubMember);
}
