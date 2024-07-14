package GDG.whatssue.domain.member.dto;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.entity.NamePolicy;
import GDG.whatssue.domain.file.entity.UploadFile;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.entity.Role;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.global.util.S3Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@Getter
@Builder
@Setter
@AllArgsConstructor
public class MemberProfileDto {

    private String clubName;
    private Long memberId;
    private String memberName;
    private String memberIntro;
    private String userEmail;
    private String userPhone;
    private Role role;
    private String profileImage;


    public static MemberProfileDto of(ClubMember clubMember) {

        String storeFileName = clubMember.getProfileImage().getStoreFileName();
        String memberProfileImage = S3Utils.getFullPath(storeFileName);

        User user = clubMember.getUser();
        Club club = clubMember.getClub();


        return MemberProfileDto.builder()
                .clubName(club.getClubName())
                .memberId(clubMember.getId())
                .memberName(clubMember.getMemberName())
                .memberIntro(clubMember.getMemberIntro())
                .userEmail(clubMember.isEmailPublic() ? user.getUserEmail() : null)
                .userPhone(clubMember.isPhonePublic() ? user.getUserPhone() : null)
                .role(clubMember.getRole())
                .profileImage(memberProfileImage)
                .build();
    }

}

