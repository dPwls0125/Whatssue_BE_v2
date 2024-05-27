package GDG.whatssue.domain.club.service;

import static GDG.whatssue.domain.file.FileConst.*;

import GDG.whatssue.domain.club.dto.ClubCreateRequest;
import GDG.whatssue.domain.club.dto.ClubCreateResponse;
import GDG.whatssue.domain.club.dto.GetClubInfoResponse;
import GDG.whatssue.domain.club.dto.UpdateClubInfoRequest;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.file.entity.UploadFile;
import GDG.whatssue.domain.file.repository.FileRepository;
import GDG.whatssue.domain.file.service.FileUploadService;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.domain.user.repository.UserRepository;
import GDG.whatssue.global.util.S3Utils;
import GDG.whatssue.global.error.CommonException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {
    private final ClubRepository clubRepository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;

    @Transactional
    public ClubCreateResponse createClub(Long userId, ClubCreateRequest requestDto, MultipartFile profileImage) throws IOException {
        User user = userRepository.findById(userId).get();

        //클럽 생성
        Club club = requestDto.toEntity();
        clubRepository.save(club);

        //프로필 사진 저장
        UploadFile clubProfileImage = fileUploadService.uploadFile(profileImage, CLUB_PROFILE_IMAGE_DIRNAME);
        club.changeProfileImage(clubProfileImage);
        fileRepository.save(clubProfileImage);

        //로그인 유저 관리자로 추가
        ClubMember newMember = ClubMember.newMember(club, user);
        newMember.switchToManager();

        return ClubCreateResponse.builder().clubId(club.getId()).build();
    }

    @Transactional
    public void updateClubInfo(Long clubId, UpdateClubInfoRequest requestDto, MultipartFile profileImage) throws IOException {
        Club club = getClub(clubId);

        //정보 update
        club.updateClubInfo(requestDto);

        //버킷 및 DB 파일 삭제
        deleteProfileImage(club);

        //프로필 사진 저장
        UploadFile clubProfileImage = fileUploadService.uploadFile(profileImage, CLUB_PROFILE_IMAGE_DIRNAME);
        club.changeProfileImage(clubProfileImage);
        fileRepository.save(clubProfileImage);
    }

    @Transactional
    public void updateClubPrivateStatus(Long clubId) {
        getClub(clubId).updateIsPrivate();
    }


    public GetClubInfoResponse getClubInfo(Long clubId) {
        Club club = getClub(clubId);

        String storeFileName = club.getProfileImage().getStoreFileName();
        String clubProfileImage = S3Utils.getFullPath(storeFileName);

        return GetClubInfoResponse.builder()
            .clubName(club.getClubName())
            .clubIntro(club.getClubIntro())
            .contactMeans(club.getContactMeans())
            .namePolicy(club.getNamePolicy())
            .privateCode(club.getPrivateCode())
            .clubProfileImage(clubProfileImage)
            .memberCount(club.getMemberCount())
            .isPrivate(club.isPrivate()).build();
    }

    @Transactional
    public void updateClubCode(Long clubId) {
        Club club = getClub(clubId);

        club.createNewPrivateCode();
    }

    public boolean isClubExist(Long clubId) {
        getClub(clubId);
        return true;
    }

    @Transactional
    public void deleteProfileImage(Club club) {
        fileUploadService.deleteFile(club.getProfileImage().getStoreFileName()); //s3에서 삭제
        fileRepository.delete(club.getProfileImage());//레포에서 삭제
    }

    public Club getClub(Long clubId) {
        return clubRepository.findById(clubId).orElseThrow(()
            -> new CommonException(ClubErrorCode.EX3100));
    }
}

