package GDG.whatssue.domain.member.service;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.entity.NamePolicy;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.file.entity.MemberProfileImage;
import GDG.whatssue.domain.file.service.FileUploadService;
import GDG.whatssue.domain.member.dto.*;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubMemberService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final FileUploadService fileUploadService;

    @Transactional
    public void modifyClubMember(Long clubId, Long userId, ModifyMemberProfileRequest request) throws IOException {

        ClubMember clubMember = clubMemberRepository.findById(getClubMemberId(clubId,userId)).get();

        clubMember.updateProfile(request, getMemberNameByNamePolicy(clubId, clubMember));

        if(request.getIsProfileImageChanged()){

            //기존 이미지 가져오기 및 삭제
            MemberProfileImage currentProfileImage = clubMember.getProfileImage();
            fileUploadService.deleteFile(currentProfileImage.getStoreFileName());

            MultipartFile multipartFile = request.getProfileImage();
            String storeFileName = fileUploadService.uploadFile(multipartFile, MEMBER_PROFILE_IMAGE_DIRNAME);
            String originalFileName = fileUploadService.getOriginalFileName(multipartFile);

            // 번호 유지 및 파일명 변경
            currentProfileImage.update(originalFileName, storeFileName);
        }
    }

    @Transactional
    public void setMemberProfile(Long clubId, Long userId, CreateMemberProfileRequest request) throws IOException {

        ClubMember clubMember = clubMemberRepository.findById(getClubMemberId(clubId,userId)).get();

        if(!clubMember.isFirstVisit()) throw new CommonException(ClubMemberErrorCode.EX2201);

        // 멤버 프로필 이미지 저장
        MultipartFile multipartFile = request.getProfileImage();
        String storeFileName = fileUploadService.uploadFile(multipartFile, MEMBER_PROFILE_IMAGE_DIRNAME);
        String originalFileName = fileUploadService.getOriginalFileName(multipartFile);
        MemberProfileImage memberProfileImage = MemberProfileImage.of(originalFileName, storeFileName);


        clubMember.updateProfile(request, getMemberNameByNamePolicy(clubId,clubMember));
        clubMember.changeProfileImage(memberProfileImage);
        clubMember.setFirstVisitFalse();

    }

    // TDDO
    @Transactional
    public MyProfileDto getMyProfile(Long clubId, Long userId) {

        ClubMember clubMember = findClubMemberByClubAndUser(clubId, userId).get();

        return MyProfileDto.of(clubMember);
    }

    public MemberProfileDto getMemberProfile(Long clubId, Long memberId){

        ClubMember clubMember = clubMemberRepository.findById(memberId).orElseThrow(()-> new CommonException(ClubMemberErrorCode.EX2100));

        if(clubMember.getClub().getId() != clubId) throw new CommonException(ClubMemberErrorCode.EX2203);

        return MemberProfileDto.of(clubMember);

    }

    public Page<ClubMemberListResponse> getClubMemberList(Long clubId, int size, int page){

        Pageable pageable = PageRequest.of(page,size);

        Page<ClubMember> clubMemberPage = clubMemberRepository.findByClubIdOrderByRole(clubId,pageable);

        List<ClubMemberListResponse> clubMemberList = clubMemberPage.stream()
                .map(ClubMemberListResponse::of)
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

    public String getMemberNameByNamePolicy(Long clubId, ClubMember member){

        Club club = clubRepository.findById(clubId).get();
        NamePolicy namePolicy = club.getNamePolicy();
        if(namePolicy == REAL_NAME)
            return member.getUser().getUserName();
        else
            return member.getMemberName();
    }

    public GetMemberInfoResponse getMemberInfo(Long clubId, Long userId) {
        return new GetMemberInfoResponse(clubMemberRepository.findMemberWithClub(clubId, userId));
    }
    @Transactional
    public void withdrawClub(Long memberId) {
        ClubMember clubMember = clubMemberRepository.findById(memberId)
                .orElseThrow(()-> new CommonException(ClubMemberErrorCode.EX2100));
        clubMemberRepository.delete(clubMember);
    }
}