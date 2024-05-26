package GDG.whatssue.domain.attendance.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberAttendanceResult is a Querydsl query type for MemberAttendanceResult
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberAttendanceResult extends EntityPathBase<MemberAttendanceResult> {

    private static final long serialVersionUID = 2068016506L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberAttendanceResult memberAttendanceResult = new QMemberAttendanceResult("memberAttendanceResult");

    public final NumberPath<Integer> absentCount = createNumber("absentCount", Integer.class);

    public final NumberPath<Integer> checkCount = createNumber("checkCount", Integer.class);

    public final GDG.whatssue.domain.member.entity.QClubMember clubMember;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> officialCount = createNumber("officialCount", Integer.class);

    public QMemberAttendanceResult(String variable) {
        this(MemberAttendanceResult.class, forVariable(variable), INITS);
    }

    public QMemberAttendanceResult(Path<? extends MemberAttendanceResult> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberAttendanceResult(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberAttendanceResult(PathMetadata metadata, PathInits inits) {
        this(MemberAttendanceResult.class, metadata, inits);
    }

    public QMemberAttendanceResult(Class<? extends MemberAttendanceResult> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.clubMember = inits.isInitialized("clubMember") ? new GDG.whatssue.domain.member.entity.QClubMember(forProperty("clubMember"), inits.get("clubMember")) : null;
    }

}

