package GDG.whatssue.domain.attendance.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QScheduleAttendanceResult is a Querydsl query type for ScheduleAttendanceResult
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScheduleAttendanceResult extends EntityPathBase<ScheduleAttendanceResult> {

    private static final long serialVersionUID = 2014293239L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QScheduleAttendanceResult scheduleAttendanceResult = new QScheduleAttendanceResult("scheduleAttendanceResult");

    public final GDG.whatssue.global.common.QBaseEntity _super = new GDG.whatssue.global.common.QBaseEntity(this);

    public final EnumPath<AttendanceType> attendanceType = createEnum("attendanceType", AttendanceType.class);

    public final GDG.whatssue.domain.member.entity.QClubMember clubMember;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final GDG.whatssue.domain.schedule.entity.QSchedule schedule;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateAt = _super.updateAt;

    public QScheduleAttendanceResult(String variable) {
        this(ScheduleAttendanceResult.class, forVariable(variable), INITS);
    }

    public QScheduleAttendanceResult(Path<? extends ScheduleAttendanceResult> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QScheduleAttendanceResult(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QScheduleAttendanceResult(PathMetadata metadata, PathInits inits) {
        this(ScheduleAttendanceResult.class, metadata, inits);
    }

    public QScheduleAttendanceResult(Class<? extends ScheduleAttendanceResult> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.clubMember = inits.isInitialized("clubMember") ? new GDG.whatssue.domain.member.entity.QClubMember(forProperty("clubMember"), inits.get("clubMember")) : null;
        this.schedule = inits.isInitialized("schedule") ? new GDG.whatssue.domain.schedule.entity.QSchedule(forProperty("schedule"), inits.get("schedule")) : null;
    }

}

