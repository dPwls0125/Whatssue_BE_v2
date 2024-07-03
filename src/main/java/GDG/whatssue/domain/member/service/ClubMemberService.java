package GDG.whatssue.domain.member.service;

import GDG.whatssue.domain.member.dto.ClubMemberDto;
import GDG.whatssue.domain.member.dto.ClubMemberInfoDto;
import GDG.whatssue.domain.member.dto.MemberAuthInfoResponse;
import GDG.whatssue.domain.member.dto.MemberProfileDto;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.user.Error.UserErrorCode;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.domain.user.repository.UserRepository;
import GDG.whatssue.global.util.S3Utils;
import GDG.whatssue.global.error.CommonException;
import java.util.Optional;
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

        ClubMember clubMember = findClubMemberByClubAndUser(clubId, userId).get();

        User user = userRepository.findById(userId)
                .orElseThrow(() ->new CommonException(UserErrorCode.EX1100));

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

    public MemberAuthInfoResponse getMemberAuthInfo(Long clubId, Long userId) {
        ClubMember member = clubMemberRepository.findMemberWithClub(clubId, userId);
        return new MemberAuthInfoResponse(member);
    }

    public ClubMemberDto getMemberIdAndRole(Long clubId, Long userId) {
        ClubMember clubMember = findClubMemberByClubAndUser(clubId, userId).get();
        return ClubMemberDto.builder()
                .memberId(clubMember.getId())
                .role(clubMember.getRole())
                .build();
    }

    public Long getClubMemberId(Long clubId, Long userId) {
        return findClubMemberByClubAndUser(clubId, userId).get().getId();
    }

    public Optional<ClubMember> findClubMemberByClubAndUser(Long clubId, Long userId) {
        return clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId);
    }
}