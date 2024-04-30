package GDG.whatssue.domain.club.service;

import static GDG.whatssue.global.common.FileConst.*;

import GDG.whatssue.domain.club.dto.ClubCreateRequest;
import GDG.whatssue.domain.club.dto.ClubCreateResponse;
import GDG.whatssue.domain.club.dto.GetClubInfoResponse;
import GDG.whatssue.domain.club.dto.GetJoinClubListResponse;
import GDG.whatssue.domain.club.dto.UpdateClubInfoRequest;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.file.entity.UploadFile;
import GDG.whatssue.domain.file.repository.FileRepository;
import GDG.whatssue.domain.file.service.FileUploadService;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.user.repository.UserRepository;
import GDG.whatssue.domain.member.entity.Role;
import GDG.whatssue.global.error.CommonException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
    private final ClubMemberRepository clubMemberRepository;
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;

    public List<GetJoinClubListResponse> getJoinClubList(Long userId) {
        List<ClubMember> clubMembers = clubMemberRepository.findByUser_UserId(userId);
        if (clubMembers.isEmpty()) {
            return new ArrayList<>();
        }

        List<GetJoinClubListResponse> responseList = clubMembers.stream()
            .map(c -> entityToJoinClubListResponse(c))
            .collect(Collectors.toList());

        return responseList;
    }

    @Transactional
    public ClubCreateResponse createClub(Long userId, ClubCreateRequest requestDto, MultipartFile profileImage) throws IOException {
        //초대코드 추가하여 클럽 생성
        Club savedClub = clubRepository.save(createDtoToClubEntity(requestDto));
        
        //프로필 사진 저장
        saveClubProfileImage(profileImage, savedClub);

        //클럽을 생성한 멤버(관리자)로 추가
        clubMemberRepository.save(
            ClubMember.builder()
            .club(savedClub)
            .user(userRepository.findById(userId).get())
            .role(Role.MANAGER)
            .isFirstVisit(true).build());

        return ClubCreateResponse.builder()
            .clubId(savedClub.getId()).build();
    }

    @Transactional
    public void updateClubInfo(Long clubId, UpdateClubInfoRequest requestDto, MultipartFile profileImage) throws IOException {
        Club club = getClub(clubId);

        //정보 update
        club.updateClubInfo(requestDto);
        //버킷 및 DB 파일 삭제
        deleteProfileImage(club);
        //버킷 및 DB 파일 저장
        saveClubProfileImage(profileImage, club);
    }

    @Transactional
    public void updateClubPrivateStatus(Long clubId) {
        Club club = getClub(clubId);

        club.updateIsPrivate();
    }

    public GetClubInfoResponse getClubInfo(Long clubId) {
        Club club = getClub(clubId);

        String storeFileName = club.getProfileImage().getStoreFileName();
        String clubProfileImage = fileUploadService.getFullPath(storeFileName);

        return GetClubInfoResponse.builder()
            .clubName(club.getClubName())
            .clubIntro(club.getClubIntro())
            .contactMeans(club.getContactMeans())
            .namePolicy(club.getNamePolicy())
            .privateCode(club.getPrivateCode())
            .clubProfileImage(clubProfileImage)
            .isPrivate(club.isPrivate()).build();
    }

    public GetJoinClubListResponse entityToJoinClubListResponse(ClubMember clubMember) {
        Club club = clubMember.getClub();

        String storeFileName = club.getProfileImage().getStoreFileName();
        String clubProfileImage = fileUploadService.getFullPath(storeFileName);

        return GetJoinClubListResponse.builder()
            .clubId(club.getId())
            .clubName(club.getClubName())
            .clubProfileImage(clubProfileImage)
            .createdAt(clubMember.getCreateAt())
            .role(clubMember.getRole()).build();
    }

    @Transactional
    public void updateClubCode(Long clubId) {
        Club club = getClub(clubId);

        club.createNewPrivateCode();
    }

    public boolean isClubExist(Long clubId) {
        clubRepository.findById(clubId)
            .orElseThrow(()-> new CommonException(ClubErrorCode.CLUB_NOT_FOUND_ERROR));

        return true;
    }

    @Transactional
    public void saveClubProfileImage(MultipartFile profileImage, Club savedClub) throws IOException {
        //s3에 저장 처리
        String storeFileName = fileUploadService.saveFile(profileImage, CLUB_PROFILE_IMAGE_DIRNAME);

        //fileDB에 저장
        String originalFileName = storeFileName;
        if (profileImage != null)  {
            originalFileName = profileImage.getOriginalFilename();
        }

        fileRepository.save(UploadFile.builder()
            .club(savedClub)
            .uploadFileName(originalFileName)
            .storeFileName(storeFileName).build());
    }

    @Transactional
    public void deleteProfileImage(Club club) {
        fileUploadService.deleteFile(club.getProfileImage().getStoreFileName()); //s3에서 삭제
        fileRepository.deleteById(club.getProfileImage().getId()); //레포에서 삭제
    }

    public Club createDtoToClubEntity(ClubCreateRequest requestDto) {
        Club club = requestDto.toEntity();
        club.createNewPrivateCode();

        return club;
    }

    public Club getClub(Long clubId) {
        return clubRepository.findById(clubId).orElseThrow(()
            -> new CommonException(ClubErrorCode.CLUB_NOT_FOUND_ERROR));
    }
}

