package GDG.whatssue.domain.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClub is a Querydsl query type for Club
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClub extends EntityPathBase<Club> {

    private static final long serialVersionUID = 875907805L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClub club = new QClub("club");

    public final GDG.whatssue.global.common.QBaseEntity _super = new GDG.whatssue.global.common.QBaseEntity(this);

    public final StringPath clubIntro = createString("clubIntro");

    public final ListPath<ClubJoinRequest, QClubJoinRequest> clubJoinRequestList = this.<ClubJoinRequest, QClubJoinRequest>createList("clubJoinRequestList", ClubJoinRequest.class, QClubJoinRequest.class, PathInits.DIRECT2);

    public final ListPath<GDG.whatssue.domain.member.entity.ClubMember, GDG.whatssue.domain.member.entity.QClubMember> clubMemberList = this.<GDG.whatssue.domain.member.entity.ClubMember, GDG.whatssue.domain.member.entity.QClubMember>createList("clubMemberList", GDG.whatssue.domain.member.entity.ClubMember.class, GDG.whatssue.domain.member.entity.QClubMember.class, PathInits.DIRECT2);

    public final StringPath clubName = createString("clubName");

    public final StringPath contactMeans = createString("contactMeans");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isPrivate = createBoolean("isPrivate");

    public final EnumPath<NamePolicy> namePolicy = createEnum("namePolicy", NamePolicy.class);

    public final ListPath<GDG.whatssue.domain.post.entity.Post, GDG.whatssue.domain.post.entity.QPost> postList = this.<GDG.whatssue.domain.post.entity.Post, GDG.whatssue.domain.post.entity.QPost>createList("postList", GDG.whatssue.domain.post.entity.Post.class, GDG.whatssue.domain.post.entity.QPost.class, PathInits.DIRECT2);

    public final StringPath privateCode = createString("privateCode");

    public final GDG.whatssue.domain.file.entity.QUploadFile profileImage;

    public final ListPath<GDG.whatssue.domain.schedule.entity.Schedule, GDG.whatssue.domain.schedule.entity.QSchedule> scheduleList = this.<GDG.whatssue.domain.schedule.entity.Schedule, GDG.whatssue.domain.schedule.entity.QSchedule>createList("scheduleList", GDG.whatssue.domain.schedule.entity.Schedule.class, GDG.whatssue.domain.schedule.entity.QSchedule.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateAt = _super.updateAt;

    public QClub(String variable) {
        this(Club.class, forVariable(variable), INITS);
    }

    public QClub(Path<? extends Club> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClub(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClub(PathMetadata metadata, PathInits inits) {
        this(Club.class, metadata, inits);
    }

    public QClub(Class<? extends Club> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.profileImage = inits.isInitialized("profileImage") ? new GDG.whatssue.domain.file.entity.QUploadFile(forProperty("profileImage"), inits.get("profileImage")) : null;
    }

}

