package GDG.whatssue.domain.schedule.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSchedule is a Querydsl query type for Schedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSchedule extends EntityPathBase<Schedule> {

    private static final long serialVersionUID = 2067967775L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSchedule schedule = new QSchedule("schedule");

    public final GDG.whatssue.global.common.QBaseEntity _super = new GDG.whatssue.global.common.QBaseEntity(this);

    public final ListPath<GDG.whatssue.domain.attendance.entity.ScheduleAttendanceResult, GDG.whatssue.domain.attendance.entity.QScheduleAttendanceResult> attendanceResultList = this.<GDG.whatssue.domain.attendance.entity.ScheduleAttendanceResult, GDG.whatssue.domain.attendance.entity.QScheduleAttendanceResult>createList("attendanceResultList", GDG.whatssue.domain.attendance.entity.ScheduleAttendanceResult.class, GDG.whatssue.domain.attendance.entity.QScheduleAttendanceResult.class, PathInits.DIRECT2);

    public final EnumPath<AttendanceStatus> attendanceStatus = createEnum("attendanceStatus", AttendanceStatus.class);

    public final GDG.whatssue.domain.club.entity.QClub club;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest, GDG.whatssue.domain.officialabsence.entity.QOfficialAbsenceRequest> OfficialAbsenceRequestList = this.<GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest, GDG.whatssue.domain.officialabsence.entity.QOfficialAbsenceRequest>createList("OfficialAbsenceRequestList", GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest.class, GDG.whatssue.domain.officialabsence.entity.QOfficialAbsenceRequest.class, PathInits.DIRECT2);

    public final GDG.whatssue.domain.member.entity.QClubMember register;

    public final StringPath scheduleContent = createString("scheduleContent");

    public final DatePath<java.time.LocalDate> scheduleDate = createDate("scheduleDate", java.time.LocalDate.class);

    public final StringPath scheduleName = createString("scheduleName");

    public final StringPath schedulePlace = createString("schedulePlace");

    public final TimePath<java.time.LocalTime> scheduleTime = createTime("scheduleTime", java.time.LocalTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateAt = _super.updateAt;

    public QSchedule(String variable) {
        this(Schedule.class, forVariable(variable), INITS);
    }

    public QSchedule(Path<? extends Schedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSchedule(PathMetadata metadata, PathInits inits) {
        this(Schedule.class, metadata, inits);
    }

    public QSchedule(Class<? extends Schedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new GDG.whatssue.domain.club.entity.QClub(forProperty("club"), inits.get("club")) : null;
        this.register = inits.isInitialized("register") ? new GDG.whatssue.domain.member.entity.QClubMember(forProperty("register"), inits.get("register")) : null;
    }

}

