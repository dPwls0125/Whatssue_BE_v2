package GDG.whatssue.domain.member.service;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.entity.NamePolicy;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.file.entity.MemberProfileImage;
import GDG.whatssue.domain.file.service.FileUploadService;
import GDG.whatssue.domain.member.dto.*;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.entity.ImgModifyStatus;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.domain.user.repository.UserRepository;
import GDG.whatssue.global.util.S3Utils;
import GDG.whatssue.global.error.CommonException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static GDG.whatssue.domain.club.entity.NamePolicy.REAL_NAME;
import static GDG.whatssue.domain.file.FileConst.MEMBER_PROFILE_IMAGE_DIRNAME;
import static GDG.whatssue.domain.member.entity.ImgModifyStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubMemberService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;

    @Transactional
    public void modifyClubMember(Long clubId, Long userId, ModifyMemberProfileRequest request) throws IOException {

        Club club = clubRepository.findById(clubId).get();
        NamePolicy namePolicy = club.getNamePolicy();

        ClubMember clubMember = clubMemberRepository.findById(getClubMemberId(clubId,userId)).get();
        ImgModifyStatus imageModifyStatus = request.getImageModifyStatus();

       if(imageModifyStatus == DELETE) {

            MemberProfileImage memberProfileImage = clubMember.getProfileImage();
            // 버킷에서 사진 삭제

           fileUploadService.deleteFile(memberProfileImage.getStoreFileName());
            // 프로필 이미지 삭제 - multipart file == null 인 경우 기본 이미지
           String storeFileName = fileUploadService.uploadFile(null, MEMBER_PROFILE_IMAGE_DIRNAME);
           String originalFileName = fileUploadService.getOriginalFileName(null);
           // 기본 이미지로 업데이트
           memberProfileImage.update(originalFileName, storeFileName);

        } else if (imageModifyStatus == MODIFY){
           if(request.getProfileImage() == null ) throw new CommonException(ClubMemberErrorCode.EX2202);

           // 멤버 프로필 이미지 저장
           MultipartFile multipartFile = request.getProfileImage();

           String storeFileName = fileUploadService.uploadFile(multipartFile, MEMBER_PROFILE_IMAGE_DIRNAME);
           String originalFileName = fileUploadService.getOriginalFileName(multipartFile);
           MemberProfileImage currentProfileImage = clubMember.getProfileImage();
           // 버킷에서 기존 사진 삭제
           fileUploadService.deleteFile(currentProfileImage.getStoreFileName());
           // DB 새로운 사진으로 업데이트
           currentProfileImage.update(originalFileName, storeFileName);

       }

        // 멤버 프로필 정보 업데이트
        String memberName;
        if(namePolicy == REAL_NAME)
            memberName = clubMember.getUser().getUserName();
        else
            memberName = request.getMemberName();

        clubMember.updateProfile(request.getMemberIntro(), memberName, request.getIsEmailPublic(), request.getIsPhonePublic());

    }

    @Transactional
    public void setMemberProfile(Long clubId, Long userId, CreateMemberProfileRequest request) throws IOException {

        Club club = clubRepository.findById(clubId).get();
        NamePolicy namePolicy = club.getNamePolicy();

        ClubMember clubMember = clubMemberRepository.findById(getClubMemberId(clubId,userId)).get();

        if(!clubMember.isFirstVisit()) throw new CommonException(ClubMemberErrorCode.EX2201);

        // 멤버 프로필 이미지 저장
        MultipartFile multipartFile = request.getProfileImage();
        String storeFileName = fileUploadService.uploadFile(multipartFile, MEMBER_PROFILE_IMAGE_DIRNAME);
        String originalFileName = fileUploadService.getOriginalFileName(multipartFile);
        MemberProfileImage memberProfileImage = MemberProfileImage.of(originalFileName, storeFileName);

        // 멤버 프로필 정보  업데이트
        String memberName;
        if(namePolicy == REAL_NAME)
            memberName = clubMember.getUser().getUserName();
        else
            memberName = request.getMemberName();

        clubMember.updateProfile(request.getMemberIntro(), memberName, request.getIsEmailPublic(), request.getIsPhonePublic());
        clubMember.changeProfileImage(memberProfileImage);
        clubMember.setFirstVisitFalse();
    }

    // TDDO
    @Transactional
    public MyProfileDto getMyProfile(Long clubId, Long userId) {

        ClubMember clubMember = findClubMemberByClubAndUser(clubId, userId).get();

        return MyProfileDto.of(clubMember);
    }


    public MemberProfileDto getMemberProfile(Long memberId){

        ClubMember clubMember = clubMemberRepository.findById(memberId).orElseThrow(()-> new CommonException(ClubMemberErrorCode.EX2100));

        return MemberProfileDto.of(clubMember);

    }

    public Page<ClubMemberListResponse> getClubMemberList(Long clubId, int size, int page){

        Pageable pageable = PageRequest.of(page,size);

        Page<ClubMember> clubMemberPage = clubMemberRepository.findByClubIdOrderByRole(clubId,pageable);

        List<ClubMemberListResponse> clubMemberList = clubMemberPage.stream()
                .map(clubMember -> ClubMemberListResponse.of(clubMember))
                .collect(Collectors.toList());

        return new PageImpl<>(clubMemberList,pageable,clubMemberPage.getTotalElements());
    }


    public MemberAuthInfoResponse getMemberAuthInfo(Long clubId, Long userId) {
        ClubMember member = clubMemberRepository.findMemberWithClub(clubId, userId);
        return new MemberAuthInfoResponse(member);
    }

    public Long getClubMemberId(Long clubId, Long userId) {
        return findClubMemberByClubAndUser(clubId, userId).get().getId();
    }

    public Optional<ClubMember> findClubMemberByClubAndUser(Long clubId, Long userId) {
        return clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId);
    }

}