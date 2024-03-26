package GDG.whatssue.domain.club.service.impl;

import GDG.whatssue.domain.club.dto.ClubCreateRequest;
import GDG.whatssue.domain.club.dto.GetClubInfoResponse;
import GDG.whatssue.domain.club.dto.UpdateClubInfoRequest;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.club.service.ClubService;
import GDG.whatssue.domain.file.entity.UploadFile;
import GDG.whatssue.domain.file.repository.FileRepository;
import GDG.whatssue.domain.file.service.FileUploadService;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.user.repository.UserRepository;
import GDG.whatssue.domain.member.entity.Role;
import GDG.whatssue.global.error.CommonException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private static String PROFILE_IMAGE_DIRNAME = "clubProfileImage";
    private static String DEFAULT_PROFILE_IMAGE = PROFILE_IMAGE_DIRNAME + "/default.png";
    private final ClubRepository clubRepository;
    private final FileRepository fileRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;

    @Override
    public Long createClub(Long userId, ClubCreateRequest requestDto, MultipartFile profileImage) throws IOException {
        //초대코드 추가하여 클럽 생성
        Club savedClub = clubRepository.save(createDtoToClubEntity(requestDto));


        if (profileImage != null) {
            String originalFileName = profileImage.getOriginalFilename();
            //profileImage fileRepository 및 s3에 저장 처리
            String storeFileName = fileUploadService.saveFile(profileImage, PROFILE_IMAGE_DIRNAME);
            fileRepository.save(UploadFile.builder()
                .club(savedClub)
                .uploadFileName(originalFileName)
                .storeFileName(storeFileName).build());
        }

        //클럽을 생성한 유저 관리자로 추가
        //따로 메서드로 뺄것. + 추가하면서 관련 테이블 생성도 해야함. TODO
        clubMemberRepository.save(
            ClubMember.builder()
            .club(savedClub)
            .user(userRepository.findById(userId).get())
            .role(Role.MANAGER)
            .isFirstVisit(true).build());

        return savedClub.getId();
    }

    @Override
    public void updateClubInfo(Long clubId, UpdateClubInfoRequest requestDto, MultipartFile profileImage) throws IOException {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new CommonException(ClubErrorCode.CLUB_NOT_FOUND_ERROR));

        club.updateClubInfo(requestDto);
        clubRepository.save(club);

        //근데 사진이 안온게 변경을 안한건지, 삭제를 한건지 어떻게 알아 삭제를 따로 구분..?TODO
        if (profileImage != null) {
            if (club.getProfileImage() != null) { //기본 이미지가 아니라면
                fileUploadService.deleteFile(club.getProfileImage().getUploadFileName());
                fileRepository.delete(club.getProfileImage());
            }

            //변경된 profileImage fileRepository 및 s3에 저장 처리
            String originalFileName = profileImage.getOriginalFilename();
            String storeFileName = fileUploadService.saveFile(profileImage, PROFILE_IMAGE_DIRNAME);
            fileRepository.save(UploadFile.builder()
                .club(club)
                .uploadFileName(originalFileName)
                .storeFileName(storeFileName).build());
        }
    }

    @Override
    public void updateClubPrivateStatus(Long clubId) {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new CommonException(ClubErrorCode.CLUB_NOT_FOUND_ERROR));

        club.updateIsPrivate();
    }

    @Override
    public GetClubInfoResponse getClubInfo(Long clubId) {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new CommonException(ClubErrorCode.CLUB_NOT_FOUND_ERROR));

        String uploadFileName = "";
        UploadFile profileImage = club.getProfileImage();

        if (profileImage != null) {
            uploadFileName = profileImage.getUploadFileName();
        }

        if (profileImage == null) {
            uploadFileName = DEFAULT_PROFILE_IMAGE;
        }

        String fullPath = fileUploadService.getFullPath(uploadFileName);

        return GetClubInfoResponse.builder()
            .clubName(club.getClubName())
            .clubIntro(club.getClubIntro())
            .contactMeans(club.getContactMeans())
            .namePolicy(club.getNamePolicy())
            .privateCode(club.getPrivateCode())
            .profileImage(fullPath)
            .isPrivate(club.isPrivate()).build();
    }

    @Override
    public void updateClubCode(Long clubId) {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new CommonException(ClubErrorCode.CLUB_NOT_FOUND_ERROR));

        club.createNewPrivateCode();
    }

    private Club createDtoToClubEntity(ClubCreateRequest requestDto) {
        Club club = requestDto.toEntity();
        club.createNewPrivateCode();

        return club;
    }
}

