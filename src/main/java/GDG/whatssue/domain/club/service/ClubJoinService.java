package GDG.whatssue.domain.club.service;

import GDG.whatssue.domain.club.dto.ClubJoinRequestDto;

public interface ClubJoinService {

    public void joinClub(Long userId, ClubJoinRequestDto requestDto);

}
