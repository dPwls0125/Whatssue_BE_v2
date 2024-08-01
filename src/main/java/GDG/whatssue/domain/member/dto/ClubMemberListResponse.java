package GDG.whatssue.domain.member.dto;

import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.global.util.S3Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClubMemberListResponse {

    private Long memberId;
    private String memberName;
    private String memberIntro;
    private String role;
    private String memberImage;


    public static ClubMemberListResponse of(ClubMember member){

        String storeFileName = member.getProfileImage().getStoreFileName();
        String memberProfileImage = S3Utils.getFullPath(storeFileName);

        return new ClubMemberListResponse(member.getId(), member.getMemberName(),member.getMemberIntro(),member.getRole().toString(),memberProfileImage);

    }

}
