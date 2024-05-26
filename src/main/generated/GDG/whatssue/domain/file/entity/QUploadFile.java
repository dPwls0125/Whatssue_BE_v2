package GDG.whatssue.domain.file.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUploadFile is a Querydsl query type for UploadFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUploadFile extends EntityPathBase<UploadFile> {

    private static final long serialVersionUID = -1802066102L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUploadFile uploadFile = new QUploadFile("uploadFile");

    public final GDG.whatssue.domain.club.entity.QClub club;

    public final GDG.whatssue.domain.member.entity.QClubMember clubMember;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final GDG.whatssue.domain.post.entity.QPost post;

    public final StringPath storeFileName = createString("storeFileName");

    public final StringPath uploadFileName = createString("uploadFileName");

    public QUploadFile(String variable) {
        this(UploadFile.class, forVariable(variable), INITS);
    }

    public QUploadFile(Path<? extends UploadFile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUploadFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUploadFile(PathMetadata metadata, PathInits inits) {
        this(UploadFile.class, metadata, inits);
    }

    public QUploadFile(Class<? extends UploadFile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new GDG.whatssue.domain.club.entity.QClub(forProperty("club"), inits.get("club")) : null;
        this.clubMember = inits.isInitialized("clubMember") ? new GDG.whatssue.domain.member.entity.QClubMember(forProperty("clubMember"), inits.get("clubMember")) : null;
        this.post = inits.isInitialized("post") ? new GDG.whatssue.domain.post.entity.QPost(forProperty("post"), inits.get("post")) : null;
    }

}

