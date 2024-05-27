package GDG.whatssue.domain.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = -1213160527L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final GDG.whatssue.global.common.QBaseEntity _super = new GDG.whatssue.global.common.QBaseEntity(this);

    public final GDG.whatssue.domain.club.entity.QClub club;

    public final ListPath<GDG.whatssue.domain.comment.entity.Comment, GDG.whatssue.domain.comment.entity.QComment> commentList = this.<GDG.whatssue.domain.comment.entity.Comment, GDG.whatssue.domain.comment.entity.QComment>createList("commentList", GDG.whatssue.domain.comment.entity.Comment.class, GDG.whatssue.domain.comment.entity.QComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<PostCategory> postCategory = createEnum("postCategory", PostCategory.class);

    public final StringPath postContent = createString("postContent");

    public final ListPath<GDG.whatssue.domain.file.entity.UploadFile, GDG.whatssue.domain.file.entity.QUploadFile> postImageFiles = this.<GDG.whatssue.domain.file.entity.UploadFile, GDG.whatssue.domain.file.entity.QUploadFile>createList("postImageFiles", GDG.whatssue.domain.file.entity.UploadFile.class, GDG.whatssue.domain.file.entity.QUploadFile.class, PathInits.DIRECT2);

    public final StringPath postTitle = createString("postTitle");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateAt = _super.updateAt;

    public final GDG.whatssue.domain.member.entity.QClubMember writer;

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new GDG.whatssue.domain.club.entity.QClub(forProperty("club"), inits.get("club")) : null;
        this.writer = inits.isInitialized("writer") ? new GDG.whatssue.domain.member.entity.QClubMember(forProperty("writer"), inits.get("writer")) : null;
    }

}

