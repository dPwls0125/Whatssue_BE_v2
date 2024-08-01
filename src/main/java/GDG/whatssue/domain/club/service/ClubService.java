package GDG.whatssue.domain.club.service;

import static GDG.whatssue.domain.file.FileConst.*;

import GDG.whatssue.domain.club.dto.CreateClubRequest;
import GDG.whatssue.domain.club.dto.CreateClubResponse;
import GDG.whatssue.domain.club.dto.GetClubInfoByPrivateCodeResponse;
import GDG.whatssue.domain.club.dto.GetClubInfoResponse;
import GDG.whatssue.domain.club.dto.UpdateClubInfoRequest;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.club.dto.GetJoinClubResponse;
import GDG.whatssue.domain.file.entity.ClubProfileImage;
import GDG.whatssue.domain.file.entity.UploadFile;
import GDG.whatssue.domain.file.repository.FileRepository;
import GDG.whatssue.domain.file.service.FileUploadService;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.domain.user.repository.UserRepository;
import GDG.whatssue.global.util.S3Utils;
import GDG.whatssue.global.error.CommonException;
import com.amazonaws.services.s3.transfer.Upload;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;
    private final ClubMemberRepository clubMemberRepository;

    public Page<GetJoinClubResponse> getJoinClubList(Long userId, Pageable pageable) {
        return clubMemberRepository.getJoinClubList(userId, pageable);
    }

    public GetClubInfoByPrivateCodeResponse findClubByPrivateCode(String privateCode) {
        Club findClub = clubRepository.findByPrivateCode(privateCode)
            .orElseThrow(() -> new CommonException(ClubErrorCode.EX3101));

        long memberCount = clubMemberRepository.countClubMember(findClub.getId());

        return new GetClubInfoByPrivateCodeResponse(findClub, memberCount);
    }

    @Transactional
    public CreateClubResponse createClub(Long userId, CreateClubRequest requestDto, MultipartFile multipartFile) throws IOException {
        User user = userRepository.findById(userId).get();

        //클럽 생성
        Club club = requestDto.toEntity();
        clubRepository.save(club);

        //클럽 프로필 사진 저장
        String storeFileName = fileUploadService.uploadFile(multipartFile, CLUB_PROFILE_IMAGE_DIRNAME);
        String originalFileName = fileUploadService.getOriginalFileName(multipartFile);

        ClubProfileImage profileImage = ClubProfileImage.of(originalFileName, storeFileName);
        club.updateProfileImage(profileImage);

        //로그인 유저 관리자로 추가
        ClubMember newMember = ClubMember.newMember(club, user);
        newMember.switchToManager();
        clubMemberRepository.save(newMember);

        return CreateClubResponse.builder().clubId(club.getId()).build();
    }

    /**
     * TODO
     */
    @Transactional
    public void updateClubInfo(Long clubId, UpdateClubInfoRequest requestDto, MultipartFile multipartFile) throws IOException {
        Club club = findClub(clubId);

        //정보 update
        club.updateClubInfo(requestDto);

        //기본 사진 또는 사진 변경
        if (requestDto.getImageIsChanged()) {
            fileUploadService.deleteFile(club.getProfileImage().getStoreFileName()); //s3에서 삭제

            //새로운 프로필 사진 저장
            String storeFileName = fileUploadService.uploadFile(multipartFile, CLUB_PROFILE_IMAGE_DIRNAME);
            String originalFileName = fileUploadService.getOriginalFileName(multipartFile);
            ClubProfileImage profileImage = ClubProfileImage.of(originalFileName, storeFileName);
            club.updateProfileImage(profileImage);
        }
    }

    @Transactional
    public void updateClubPrivateStatus(Long clubId, boolean isPrivate) {
        findClub(clubId).updateIsPrivate(isPrivate);
    }

    public GetClubInfoResponse getClubInfo(Long clubId) {
        Club club = findClub(clubId);

        long memberCount = clubMemberRepository.countClubMember(club.getId());

        String storeFileName = club.getProfileImage().getStoreFileName();
        String clubProfileImage = S3Utils.getFullPath(storeFileName);

        return new GetClubInfoResponse(club, clubProfileImage, memberCount);
    }

    @Transactional
    public String updateClubCode(Long clubId) {
        return findClub(clubId).updatePrivateCode();
    }

    public boolean isClubExist(Long clubId) {
        findClub(clubId);
        return true;
    }

    private Club findClub(Long clubId) {
        return clubRepository.findById(clubId).orElseThrow(()
            -> new CommonException(ClubErrorCode.EX3100));
    }
}

