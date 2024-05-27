package GDG.whatssue.domain.member.service;

//import GDG.whatssue.domain.member.dto.ClubJoinRequestDto;
//import GDG.whatssue.domain.member.entity.ClubJoinRequest;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.member.dto.ClubMemberDto;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.entity.Role;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
//import GDG.whatssue.domain.member.repository.ClubJoinRequestRepository;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubMemberManagingService {

    private final ClubMemberRepository clubMemberRepository;

    public void deleteClubMember(Long memberId) {
        ClubMember clubMember = clubMemberRepository.findById(memberId)
            .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100));

        clubMemberRepository.delete(clubMember);
    }

    public void modifyClubMemberRole(Long memberId, String role) {
        ClubMember clubMember = clubMemberRepository.findById(memberId)
            .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100));

        if (role.toUpperCase().equals(Role.MANAGER.toString()))
            clubMember.switchToManager();
        else if (role.toUpperCase().equals(Role.MEMBER.toString()))
            clubMember.switchToMember();

        clubMemberRepository.save(clubMember);
    }

}
