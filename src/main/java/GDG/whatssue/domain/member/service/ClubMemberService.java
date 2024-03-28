package GDG.whatssue.domain.member.service;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.member.dto.ClubJoinRequestDto;
import GDG.whatssue.domain.member.entity.ClubJoinRequest;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.entity.Role;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
import GDG.whatssue.domain.member.repository.ClubJoinRequestRepository;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.domain.user.repository.UserRepository;
import GDG.whatssue.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubMemberService {

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final ClubJoinRequestRepository clubJoinRequestRepository;
    private final ClubMemberRepository clubMemberRepository;

    public void addClubJoinRequest(Long userId, ClubJoinRequestDto requestDto) {
        Club club = clubRepository.findById(requestDto.getClubId())
            .orElseThrow(() -> new CommonException(ClubMemberErrorCode.CLUB_NOT_FOUND_ERROR));

//        checkPrivateCode(requestDto, club);

        User loginUser = userRepository.findById(userId).get();
        checkJoinDuplicate(loginUser, club);

        ClubJoinRequest clubJoinRequest = ClubJoinRequest.builder()
            .club(club).user(loginUser).build();

        clubJoinRequestRepository.save(clubJoinRequest);
    }

//    private static void checkPrivateCode(ClubJoinRequestDto requestDto, Club club) {
//        if (club.isPrivate()) {
//            String requestCode = requestDto.getPrivateCode();
//            String clubCode = club.getPrivateCode();
//
//            if (requestCode == null) {
//                throw new CommonException(ClubMemberErrorCode.PRIVATE_CLUB_ERROR);
//            }
//
//            if (!requestCode.equals(clubCode)) {
//                throw new CommonException(ClubMemberErrorCode.INVALID_PRIVATE_CODE_ERROR);
//            }
//        }
//    }

    private void checkJoinDuplicate(User loginUser, Club club) {
        boolean result = clubJoinRequestRepository.findAll()
            .stream()
            .anyMatch(r -> r.getClub().equals(club) && r.getUser().equals(loginUser));

        if (result) {
            throw new CommonException(ClubMemberErrorCode.DUPLICATE_CLUB_JOIN_ERROR);
        }
    }

    public void deleteClubMember(Long memberId) {
        ClubMember clubMember = clubMemberRepository.findById(memberId)
            .orElseThrow(() -> new CommonException(ClubMemberErrorCode.CLUB_MEMBER_NOT_FOUND_ERROR));
        try{
            clubMemberRepository.delete(clubMember);
        }catch(Exception e){
            throw new CommonException(ClubMemberErrorCode.CLUB_MEMBER_COULD_NOT_DELETE_ERROR);
        }
    }

    public void modifyClubMemberRole(Long memberId, String role) {
        ClubMember clubMember = clubMemberRepository.findById(memberId)
            .orElseThrow(() -> new CommonException(ClubMemberErrorCode.CLUB_MEMBER_NOT_FOUND_ERROR));
        try{
            if (role.toUpperCase().equals(Role.MANAGER.toString()))
                clubMember.setRole(Role.MANAGER);
            else if (role.toUpperCase().equals(Role.MEMBER.toString()))
                clubMember.setRole(Role.MEMBER);
            else
                throw new CommonException(ClubMemberErrorCode.CLUB_MEMBER_COULD_NOT_MODIFY_ERROR);
            clubMemberRepository.save(clubMember);
        }catch(Exception e){
            throw new CommonException(ClubMemberErrorCode.CLUB_MEMBER_COULD_NOT_MODIFY_ERROR);
        }
    }
}
