package GDG.whatssue.domain.club.service;

import GDG.whatssue.domain.club.dto.ClubCreateRequest;

public interface ClubService {

    public Long createClub(Long userId, ClubCreateRequest requestDto);
    //delete 메서드
}
