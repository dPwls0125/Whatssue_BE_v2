package GDG.whatssue.domain.club.service.impl;

import GDG.whatssue.domain.club.dto.ClubJoinRequestDto;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.club.repository.ClubJoinRequestRepository;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.club.service.ClubJoinService;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubJoinServiceImpl implements ClubJoinService {
    private final ClubRepository clubRepository;
    private final ClubJoinRequestRepository clubJoinRequestRepository;

    @Override
    public void joinClub(Long userId, ClubJoinRequestDto requestDto) {
        //가입 코드로 클럽찾기. 예외처리 TODO

        //중복 신청 또는 이미 가입 체크. 예외처리 TODO

        //가입 신청 만들기 TODO
        
    }

    private void checkJoinDuplicate(User loginUser, Club club) {
        boolean result = clubJoinRequestRepository.findAll()
            .stream()
            .anyMatch(r -> r.getClub().equals(club) && r.getUser().equals(loginUser));

        //이미 가입된 모임도 체크 TODO

        if (result) {
            throw new CommonException(ClubErrorCode.DUPLICATE_CLUB_JOIN_ERROR);
        }
    }
}
