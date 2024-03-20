package GDG.whatssue.domain.club.service.impl;

import GDG.whatssue.domain.club.dto.ClubCreateRequest;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.club.service.ClubService;
import GDG.whatssue.domain.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final FileRepository fileRepository;

    @Override
    public Long createClub(ClubCreateRequest requestDto) {
        MultipartFile profileImage = requestDto.getProfileImage();
        //profileImage fileRepository 및 s3에 저장 처리 TODO

        Club savedClub = clubRepository.save(requestDto.toEntity());
        return savedClub.getId();
    }
}
