package GDG.whatssue.domain.clubjoinrequest.service;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.clubjoinrequest.dto.GetClubInfoByPrivateCodeResponse;
import GDG.whatssue.domain.clubjoinrequest.dto.GetRejectionReasonResponse;
import GDG.whatssue.domain.clubjoinrequest.dto.GetJoinRequestsResponse;
import GDG.whatssue.domain.clubjoinrequest.entity.ClubJoinRequest;
import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.clubjoinrequest.repository.ClubJoinRequestRepository;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.clubjoinrequest.dto.GetJoinClubResponse;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.domain.user.repository.UserRepository;
import GDG.whatssue.global.error.CommonException;
import GDG.whatssue.global.util.S3Utils;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubJoinService {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final ClubJoinRequestRepository clubJoinRequestRepository;
    private final ClubMemberRepository clubMemberRepository;

    @Transactional
    public void joinClub(Long userId, Long clubId) {
        User loginUser = userRepository.findById(userId).get();
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new CommonException(ClubErrorCode.EX3100));

        //중복 신청 또는 이미 가입 체크
        checkJoinDuplicate(userId, clubId);
        checkJoinRequestDuplicate(userId, clubId);

        //가입 신청 만들기
        clubJoinRequestRepository.save(ClubJoinRequest.createClubJoinRequest(club, loginUser));
    }

    public Page<GetJoinRequestsResponse> getJoinRequests(Long userId, Pageable pageable) {
        return clubJoinRequestRepository.findAllJoinRequest(userId, pageable);
    }

    public GetRejectionReasonResponse getJoinRequestRejectionReason(Long userId, Long joinRequestId) {
        ClubJoinRequest joinRequest = clubJoinRequestRepository
            .findByIdAndUser_UserId(joinRequestId, userId)
            .orElseThrow(() -> new CommonException(ClubErrorCode.EX3102));

        return new GetRejectionReasonResponse(joinRequest.getId(), joinRequest.fetchRejectionReason());
    }

    public GetClubInfoByPrivateCodeResponse findClubByPrivateCode(String privateCode) {
        Club club = clubRepository.findByPrivateCode(privateCode)
            .orElseThrow(() -> new CommonException(ClubErrorCode.EX3101));

        return GetClubInfoByPrivateCodeResponse.builder()
            .clubId(club.getId())
            .clubProfileImage(
                S3Utils.getFullPath(club.getProfileImage().getStoreFileName())
            )
            .clubName(club.getClubName())
            .clubMemberCount(
                clubMemberRepository.getClubMemberCount(club.getId())
            )
            .namePolicy(club.getNamePolicy())
            .createdAt(LocalDate.from(club.getCreateAt()))
            .clubIntro(club.getClubIntro())
            .build();
    }

    private void checkJoinRequestDuplicate(Long userId, Long clubId) {
        if (clubJoinRequestRepository.existsByClub_IdAndUser_UserId(clubId, userId)) {
            throw new CommonException(ClubErrorCode.EX3201);
        }
    }

    private void checkJoinDuplicate(Long userId, Long clubId) {
        if (clubMemberRepository.existsByClub_IdAndUser_UserId(clubId, userId)) {
            throw new CommonException(ClubErrorCode.EX3200);
        }
    }
}
