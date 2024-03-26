package GDG.whatssue.domain.club.service;

import GDG.whatssue.domain.club.dto.ClubCreateRequest;
import GDG.whatssue.domain.club.dto.GetClubInfoResponse;
import GDG.whatssue.domain.club.dto.UpdateClubInfoRequest;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ClubService {

    public Long createClub(Long userId, ClubCreateRequest requestDto, MultipartFile profileImage) throws IOException;
    public void updateClubInfo(Long clubId, UpdateClubInfoRequest requestDto, MultipartFile profileImage) throws IOException;
    public void updateClubCode(Long clubId);
    public void updateClubPrivateStatus(Long clubId);

    public GetClubInfoResponse getClubInfo(Long clubId);

}
