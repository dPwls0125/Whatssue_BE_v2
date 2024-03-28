package GDG.whatssue.domain.club.service.impl;

import GDG.whatssue.domain.club.dto.ClubCreateRequest;
import GDG.whatssue.domain.club.dto.ClubCreateResponse;
import GDG.whatssue.domain.club.dto.GetClubInfoResponse;
import GDG.whatssue.domain.club.dto.GetJoinClubListResponse;
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
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
    public List<GetJoinClubListResponse> getJoinClubList(Long userId) {
        List<ClubMember> clubMembers = clubMemberRepository.findByUser_UserId(userId);
        if (clubMembers.isEmpty()) {
            return new ArrayList<>();
        }

        List<GetJoinClubListResponse> responseList = clubMembers.stream()
            .map(c -> GetJoinClubListResponse.builder()
                .clubId(c.getClub().getId())
                .clubName(c.getClub().getClubName())
                .profileImage(c.getClub().getProfileImage().getStoreFileName())
                .createdAt(c.getCreateAt())
                .role(c.getRole()).build())
            .collect(Collectors.toList());

        return responseList;
    }
    @Override
    public ClubCreateResponse createClub(Long userId, ClubCreateRequest requestDto, MultipartFile profileImage) throws IOException {
        //초대코드 추가하여 클럽 생성
        Club savedClub = clubRepository.save(createDtoToClubEntity(requestDto));

        if (profileImage != null) {
            saveProfileImage(profileImage, savedClub);
        }

        //클럽을 생성한 멤버(관리자)로 추가
        //따로 메서드로 뺄것. + 추가하면서 관련 테이블 생성도 해야함. TODO
        clubMemberRepository.save(
            ClubMember.builder()
            .club(savedClub)
            .user(userRepository.findById(userId).get())
            .role(Role.MANAGER)
            .isFirstVisit(true).build());

        return ClubCreateResponse.builder()
            .clubId(savedClub.getId()).build();
    }

    @Override
    public void updateClubInfo(Long clubId, UpdateClubInfoRequest requestDto, MultipartFile profileImage) throws IOException {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new CommonException(ClubErrorCode.CLUB_NOT_FOUND_ERROR));

        club.updateClubInfo(requestDto);
        clubRepository.save(club);

        if (profileImage != null) { //삭제 또는 삭제 후 재업
            if (club.getProfileImage() != null) { //기본 이미지가 아니라면
                deleteProfileImage(club);
            }

            if (!profileImage.isEmpty()) { //재업인 경우
                //변경된 profileImage fileRepository 및 s3에 저장 처리
                saveProfileImage(profileImage, club);
            }
        }
    }

    @Override
    @Transactional
    public void updateClubPrivateStatus(Long clubId) {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new CommonException(ClubErrorCode.CLUB_NOT_FOUND_ERROR));

        club.updateIsPrivate();
    }

    @Override
    public GetClubInfoResponse getClubInfo(Long clubId) {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new CommonException(ClubErrorCode.CLUB_NOT_FOUND_ERROR));

        String storeFileName = "";
        UploadFile profileImage = club.getProfileImage();

        if (profileImage != null) {
            storeFileName = profileImage.getStoreFileName();
        }

        if (profileImage == null) {
            storeFileName = DEFAULT_PROFILE_IMAGE;
        }

        String fullPath = fileUploadService.getFullPath(storeFileName);

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
    @Transactional
    public void updateClubCode(Long clubId) {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new CommonException(ClubErrorCode.CLUB_NOT_FOUND_ERROR));

        club.createNewPrivateCode();
    }

    @Transactional
    private void saveProfileImage(MultipartFile profileImage, Club savedClub) throws IOException {
        String originalFileName = profileImage.getOriginalFilename();
        //profileImage fileRepository 및 s3에 저장 처리
        String storeFileName = fileUploadService.saveFile(profileImage, PROFILE_IMAGE_DIRNAME);

        fileRepository.save(UploadFile.builder()
            .club(savedClub)
            .uploadFileName(originalFileName)
            .storeFileName(storeFileName).build());
    }

    private void deleteProfileImage(Club club) {
        fileUploadService.deleteFile(club.getProfileImage().getStoreFileName()); //s3에서 삭제
        fileRepository.deleteById(club.getProfileImage().getId()); //레포에서 삭제
    }

    private Club createDtoToClubEntity(ClubCreateRequest requestDto) {
        Club club = requestDto.toEntity();
        club.createNewPrivateCode();

        return club;
    }
}

