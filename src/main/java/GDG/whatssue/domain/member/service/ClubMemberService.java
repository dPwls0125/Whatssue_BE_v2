package GDG.whatssue.domain.member.service;

import GDG.whatssue.domain.member.dto.ClubMemberDto;
import GDG.whatssue.domain.member.dto.ClubMemberInfoDto;
import GDG.whatssue.domain.member.dto.MemberProfileDto;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.domain.user.repository.UserRepository;
import GDG.whatssue.global.util.S3Utils;
import GDG.whatssue.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClubMemberService {
    private final ClubMemberRepository clubMemberRepository;
    private final UserRepository userRepository;

    public void modifyClubMember(Long memberId, ClubMemberInfoDto requestDto) {
        ClubMember clubMember = clubMemberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100));

        clubMember.updateProfile(requestDto.getMemberName(),
                requestDto.getMemberIntro(),
                requestDto.isEmailPublic(),
                requestDto.isPhonePublic());
        clubMemberRepository.save(clubMember);
    }

    // TDDO
    @Transactional
    public MemberProfileDto getMemberProfile(Long clubId, Long userId) {

        ClubMember clubMember = getClubMember(clubId, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() ->new RuntimeException("User Not Found"));

        String storeFileName = clubMember.getProfileImage().getStoreFileName();
        String memberProfileImage = S3Utils.getFullPath(storeFileName);

        MemberProfileDto profile = MemberProfileDto.builder()
                .memberId(clubMember.getId())
                .userName(user.getUserName())
                .userPhone(user.getUserPhone())
                .userEmail(user.getUserEmail())
                .memberName(clubMember.getMemberName())
                .memberIntro(clubMember.getMemberIntro())
                .role(clubMember.getRole())
                .isMemberEmailPublic(clubMember.isEmailPublic())
                .isMemberPhonePublic(clubMember.isPhonePublic())
                .profileImage(memberProfileImage)
                .build();

        return profile;
    }


    public ClubMemberDto getMemberIdAndRole(Long clubId, Long userId) {
        ClubMember clubMember = getClubMember(clubId, userId);
        return ClubMemberDto.builder()
                .memberId(clubMember.getId())
                .role(clubMember.getRole())
                .build();
    }

    public boolean isClubMember(Long clubId, Long userId) {
        return getClubMember(clubId, userId) != null;
    }

    public boolean isClubManager(Long clubId, Long userId) {
        return getClubMember(clubId, userId).checkManagerRole();
    }
    public boolean isFirstVisit(Long clubId, Long userId) {
        return getClubMember(clubId, userId).isFirstVisit();
    }

    public Long getClubMemberId(Long clubId, Long userId) {
        return getClubMember(clubId, userId).getId();
    }

    public ClubMember getClubMember(Long clubId, Long userId) {
        return clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId).orElse(null);
    }


}