package GDG.whatssue.domain.member.dto;

import GDG.whatssue.domain.file.entity.UploadFile;
import GDG.whatssue.domain.member.entity.Role;
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
    private String userName;
    private String userEmail;
    private String userPhone;
    private String memberName;
    private String memberIntro;
    private Role role;
    private boolean isMemberEmailPublic;
    private boolean isMemberPhonePublic;
    private String profileImage;




}
