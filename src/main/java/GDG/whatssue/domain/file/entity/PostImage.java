package GDG.whatssue.domain.file.entity;

import GDG.whatssue.domain.post.entity.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage extends UploadFile{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private int orderNum;

    public void setPost(Post post) {
        this.post = post;
    }

    public void changeOrder(int orderNum) {
        this.orderNum = orderNum;
    }

    //==생성 메서드==//
    private PostImage(String uploadFileName, String storeFileName, int orderNum) {
        super(uploadFileName, storeFileName);

        changeOrder(orderNum);
    }

    public static PostImage of(String uploadFileName, String storeFileName, int orderNum) {
        return new PostImage(uploadFileName, storeFileName, orderNum);
    }
}
