package GDG.whatssue.domain.member.dto;

import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.global.util.S3Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
@AllArgsConstructor
public class MyProfileDto {

    private Long memberId;
    private String memberName;
    private String memberIntro;
    private String userName;
    private String userPhone;
    private String userEmail;
    private String role;
    private Boolean isEmailPublic;
    private Boolean isPhonePublic;
    private String profileImage;

    public static MyProfileDto of(ClubMember clubMember){

        User user = clubMember.getUser();
        String storeFileName = clubMember.getProfileImage().getStoreFileName();
        String memberProfileImage = S3Utils.getFullPath(storeFileName);

        return  MyProfileDto.builder()
                .memberId(clubMember.getId())
                .userName(user.getUserName())
                .userPhone(clubMember.isPhonePublic() ? user.getUserPhone() : null)
                .userEmail(clubMember.isEmailPublic() ? user.getUserEmail() : null)
                .memberName(clubMember.getMemberName())
                .memberIntro(clubMember.getMemberIntro())
                .role(clubMember.getRole().toString())
                .isEmailPublic(clubMember.isEmailPublic())
                .isPhonePublic(clubMember.isPhonePublic())
                .profileImage(memberProfileImage)
                .build();
    }
    }

