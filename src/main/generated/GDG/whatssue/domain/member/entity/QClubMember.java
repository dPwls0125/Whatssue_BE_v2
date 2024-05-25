package GDG.whatssue.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClubMember is a Querydsl query type for ClubMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClubMember extends EntityPathBase<ClubMember> {

    private static final long serialVersionUID = 1397958587L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClubMember clubMember = new QClubMember("clubMember");

    public final GDG.whatssue.global.common.QBaseEntity _super = new GDG.whatssue.global.common.QBaseEntity(this);

    public final GDG.whatssue.domain.club.entity.QClub club;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isEmailPublic = createBoolean("isEmailPublic");

    public final BooleanPath isFirstVisit = createBoolean("isFirstVisit");

    public final BooleanPath isPhonePublic = createBoolean("isPhonePublic");

    public final GDG.whatssue.domain.attendance.entity.QMemberAttendanceResult memberAttendanceResult;

    public final StringPath memberIntro = createString("memberIntro");

    public final StringPath memberName = createString("memberName");

    public final ListPath<GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest, GDG.whatssue.domain.officialabsence.entity.QOfficialAbsenceRequest> OfficialAbsenceRequestList = this.<GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest, GDG.whatssue.domain.officialabsence.entity.QOfficialAbsenceRequest>createList("OfficialAbsenceRequestList", GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest.class, GDG.whatssue.domain.officialabsence.entity.QOfficialAbsenceRequest.class, PathInits.DIRECT2);

    public final ListPath<GDG.whatssue.domain.post.entity.Post, GDG.whatssue.domain.post.entity.QPost> postList = this.<GDG.whatssue.domain.post.entity.Post, GDG.whatssue.domain.post.entity.QPost>createList("postList", GDG.whatssue.domain.post.entity.Post.class, GDG.whatssue.domain.post.entity.QPost.class, PathInits.DIRECT2);

    public final GDG.whatssue.domain.file.entity.QUploadFile profileImage;

    public final EnumPath<Role> role = createEnum("role", Role.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateAt = _super.updateAt;

    public final GDG.whatssue.domain.user.entity.QUser user;

    public QClubMember(String variable) {
        this(ClubMember.class, forVariable(variable), INITS);
    }

    public QClubMember(Path<? extends ClubMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClubMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClubMember(PathMetadata metadata, PathInits inits) {
        this(ClubMember.class, metadata, inits);
    }

    public QClubMember(Class<? extends ClubMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new GDG.whatssue.domain.club.entity.QClub(forProperty("club"), inits.get("club")) : null;
        this.memberAttendanceResult = inits.isInitialized("memberAttendanceResult") ? new GDG.whatssue.domain.attendance.entity.QMemberAttendanceResult(forProperty("memberAttendanceResult"), inits.get("memberAttendanceResult")) : null;
        this.profileImage = inits.isInitialized("profileImage") ? new GDG.whatssue.domain.file.entity.QUploadFile(forProperty("profileImage"), inits.get("profileImage")) : null;
        this.user = inits.isInitialized("user") ? new GDG.whatssue.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

