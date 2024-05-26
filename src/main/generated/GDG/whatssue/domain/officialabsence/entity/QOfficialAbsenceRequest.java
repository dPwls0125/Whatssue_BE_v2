package GDG.whatssue.domain.officialabsence.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOfficialAbsenceRequest is a Querydsl query type for OfficialAbsenceRequest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOfficialAbsenceRequest extends EntityPathBase<OfficialAbsenceRequest> {

    private static final long serialVersionUID = 547325016L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOfficialAbsenceRequest officialAbsenceRequest = new QOfficialAbsenceRequest("officialAbsenceRequest");

    public final GDG.whatssue.domain.member.entity.QClubMember clubMember;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath officialAbsenceContent = createString("officialAbsenceContent");

    public final EnumPath<OfficialAbsenceRequestType> officialAbsenceRequestType = createEnum("officialAbsenceRequestType", OfficialAbsenceRequestType.class);

    public final GDG.whatssue.domain.schedule.entity.QSchedule schedule;

    public QOfficialAbsenceRequest(String variable) {
        this(OfficialAbsenceRequest.class, forVariable(variable), INITS);
    }

    public QOfficialAbsenceRequest(Path<? extends OfficialAbsenceRequest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOfficialAbsenceRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOfficialAbsenceRequest(PathMetadata metadata, PathInits inits) {
        this(OfficialAbsenceRequest.class, metadata, inits);
    }

    public QOfficialAbsenceRequest(Class<? extends OfficialAbsenceRequest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.clubMember = inits.isInitialized("clubMember") ? new GDG.whatssue.domain.member.entity.QClubMember(forProperty("clubMember"), inits.get("clubMember")) : null;
        this.schedule = inits.isInitialized("schedule") ? new GDG.whatssue.domain.schedule.entity.QSchedule(forProperty("schedule"), inits.get("schedule")) : null;
    }

}

