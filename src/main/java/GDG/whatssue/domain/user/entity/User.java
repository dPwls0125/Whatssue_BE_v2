package GDG.whatssue.domain.user.entity;

import GDG.whatssue.domain.user.dto.SignUpRequestDto;
import GDG.whatssue.domain.user.dto.UserDto;
import GDG.whatssue.domain.user.dto.UserModifiyRequestDto;
import GDG.whatssue.global.common.BaseEntity;
import GDG.whatssue.domain.clubjoinrequest.entity.ClubJoinRequest;
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


    public UserDto entityToUserDto() {
        return UserDto.builder()
                .userId(this.userId)
                .userName(this.userName)
                .userPhone(this.userPhone)
                .userEmail(this.userEmail)
                .oauth2Id(this.oauth2Id)
                .build();
    }


    public void setSignUpUserInfo(SignUpRequestDto request){
        this.userName = request.getUserName();
        this.userPhone = request.getUserPhone();
        this.userEmail = request.getUserEmail();
    }

    public void setModifyUserInfo(UserModifiyRequestDto request){
        this.userName = request.getUserName();
        this.userPhone = request.getUserPhone();
        this.userEmail = request.getUserEmail();
    }

}
