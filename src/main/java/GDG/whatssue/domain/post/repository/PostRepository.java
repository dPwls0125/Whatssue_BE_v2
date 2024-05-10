package GDG.whatssue.domain.post.repository;

import GDG.whatssue.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);
}
