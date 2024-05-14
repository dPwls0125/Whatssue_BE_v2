package GDG.whatssue.domain.club.dto;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.entity.NamePolicy;
import GDG.whatssue.domain.file.entity.UploadFile;
import GDG.whatssue.domain.member.entity.ClubMember;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ClubCreateRequest {

    private String clubName; //클럽 이름(필수)
    private String clubIntro; //클럽 소개(필수)
    private Boolean isPrivate; //비공개 여부(필수)
    private String contactMeans; //연락수단
    private NamePolicy namePolicy;

    public Club toEntity(UploadFile profileImage) {
        return Club.of(clubName, clubIntro, isPrivate, contactMeans, namePolicy);
    }
}
