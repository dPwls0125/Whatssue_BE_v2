package GDG.whatssue.domain.post.entity;

import GDG.whatssue.domain.comment.entity.Comment;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Post {
    @Id
    private Long id;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList;
}
