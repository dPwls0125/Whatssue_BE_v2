package GDG.whatssue.domain.like.repository;

import GDG.whatssue.domain.like.entity.PostLike;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostAndClubMemberAndPost_Club_Id(Post post, ClubMember clubMember, Long clubId);
}
