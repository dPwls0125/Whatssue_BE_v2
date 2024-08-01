package GDG.whatssue.domain.comment.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildCommentAddDto extends CommentAddDto{

    private Long parentId;

    public ChildCommentAddDto (Long postId, String content, Long parentId){
        super(postId, content);
        this.parentId = parentId;
    }


}
