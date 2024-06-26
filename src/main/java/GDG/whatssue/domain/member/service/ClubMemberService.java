package GDG.whatssue.domain.member.service;

public interface ClubMemberService {

    public boolean isClubMember(Long clubId, Long userId);
    public boolean isClubManager(Long clubId, Long userId);
    public boolean isFirstVisit(Long clubId, Long userId);
    public Long getClubMemberId(Long clubId, Long userId);
}
