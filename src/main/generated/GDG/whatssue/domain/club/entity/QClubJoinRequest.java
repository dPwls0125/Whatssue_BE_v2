package GDG.whatssue.domain.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClubJoinRequest is a Querydsl query type for ClubJoinRequest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClubJoinRequest extends EntityPathBase<ClubJoinRequest> {

    private static final long serialVersionUID = -1809691928L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClubJoinRequest clubJoinRequest = new QClubJoinRequest("clubJoinRequest");

    public final GDG.whatssue.global.common.QBaseEntity _super = new GDG.whatssue.global.common.QBaseEntity(this);

    public final QClub club;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateAt = _super.updateAt;

    public final GDG.whatssue.domain.user.entity.QUser user;

    public QClubJoinRequest(String variable) {
        this(ClubJoinRequest.class, forVariable(variable), INITS);
    }

    public QClubJoinRequest(Path<? extends ClubJoinRequest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClubJoinRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClubJoinRequest(PathMetadata metadata, PathInits inits) {
        this(ClubJoinRequest.class, metadata, inits);
    }

    public QClubJoinRequest(Class<? extends ClubJoinRequest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new QClub(forProperty("club"), inits.get("club")) : null;
        this.user = inits.isInitialized("user") ? new GDG.whatssue.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

