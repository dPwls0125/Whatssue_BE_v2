package GDG.whatssue.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1339220359L;

    public static final QUser user = new QUser("user");

    public final GDG.whatssue.global.common.QBaseEntity _super = new GDG.whatssue.global.common.QBaseEntity(this);

    public final ListPath<GDG.whatssue.domain.club.entity.ClubJoinRequest, GDG.whatssue.domain.club.entity.QClubJoinRequest> clubJoinRequestList = this.<GDG.whatssue.domain.club.entity.ClubJoinRequest, GDG.whatssue.domain.club.entity.QClubJoinRequest>createList("clubJoinRequestList", GDG.whatssue.domain.club.entity.ClubJoinRequest.class, GDG.whatssue.domain.club.entity.QClubJoinRequest.class, PathInits.DIRECT2);

    public final ListPath<GDG.whatssue.domain.member.entity.ClubMember, GDG.whatssue.domain.member.entity.QClubMember> clubMemberList = this.<GDG.whatssue.domain.member.entity.ClubMember, GDG.whatssue.domain.member.entity.QClubMember>createList("clubMemberList", GDG.whatssue.domain.member.entity.ClubMember.class, GDG.whatssue.domain.member.entity.QClubMember.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final StringPath oauth2Id = createString("oauth2Id");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateAt = _super.updateAt;

    public final StringPath userEmail = createString("userEmail");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final StringPath userName = createString("userName");

    public final StringPath userPhone = createString("userPhone");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

