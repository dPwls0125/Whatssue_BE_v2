package GDG.whatssue.domain.club.service;

import GDG.whatssue.domain.club.dto.ClubJoinRequestDto;
import GDG.whatssue.domain.club.dto.GetJoinRequestsResponse;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.entity.ClubJoinRequest;
import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.club.repository.ClubJoinRequestRepository;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.domain.user.repository.UserRepository;
import GDG.whatssue.global.error.CommonException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
    public void joinClub(Long userId, ClubJoinRequestDto requestDto) {
        User loginUser = userRepository.findById(userId).get();
        String privateCode = requestDto.getPrivateCode();

        //가입 코드로 클럽찾기
        Club club = clubRepository.findByPrivateCode(privateCode)
            .orElseThrow(() -> new CommonException(ClubErrorCode.EX3101));

        //중복 신청 또는 이미 가입 체크
        checkJoinDuplicate(userId, club.getId());
        checkJoinRequestDuplicate(userId, club.getId());

        //가입 신청 만들기
        clubJoinRequestRepository.save(ClubJoinRequest.of(club, loginUser));
    }

    public List<GetJoinRequestsResponse> getJoinRequests(Long userId) {
        List<ClubJoinRequest> joinRequests = clubJoinRequestRepository.findByUser_UserId(userId);

        if (joinRequests.isEmpty()) {
            return new ArrayList<>();
        }

        List<GetJoinRequestsResponse> responseList = joinRequests.stream()
            .map(r -> GetJoinRequestsResponse.builder()
                .clubName(r.getClub().getClubName())
                .createdAt(r.getCreateAt()).build())
            .collect(Collectors.toList());

        return responseList;
    }

    private void checkJoinRequestDuplicate(Long userId, Long clubId) {
        //이미 가입 신청한 모임
        boolean result = clubJoinRequestRepository.existsByClub_IdAndUser_UserId(clubId, userId);

        if (result) {
            throw new CommonException(ClubErrorCode.EX3201);
        }
    }

    private void checkJoinDuplicate(Long userId, Long clubId) {
        //이미 가입된 모임
        boolean result = clubMemberRepository.existsByClub_IdAndUser_UserId(clubId, userId);

        if (result) {
            throw new CommonException(ClubErrorCode.EX3200);
        }
    }
}
