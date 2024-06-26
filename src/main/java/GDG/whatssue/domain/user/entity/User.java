package GDG.whatssue.domain.user.entity;

import GDG.whatssue.global.common.BaseEntity;
import GDG.whatssue.domain.club.entity.ClubJoinRequest;
import GDG.whatssue.domain.member.entity.ClubMember;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String userEmail;

    @Column
    private String userName;

    @Column( nullable = false, unique = true)
    private String oauth2Id;

    @Column
    private String userPhone;

    @OneToMany(mappedBy = "user")
    private List<ClubJoinRequest> clubJoinRequestList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ClubMember> clubMemberList = new ArrayList<>();

}
