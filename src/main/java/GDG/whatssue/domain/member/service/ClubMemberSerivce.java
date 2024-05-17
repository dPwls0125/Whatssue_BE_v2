package GDG.whatssue.domain.member.service;

import GDG.whatssue.domain.file.service.FileUploadService;
import GDG.whatssue.domain.member.dto.ClubMemberDto;
import GDG.whatssue.domain.member.dto.MemberProfileDto;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.domain.user.repository.UserRepository;
import GDG.whatssue.global.error.CommonException;
import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
@RequiredArgsConstructor
public class ClubMemberSerivce {
    private final ClubMemberRepository clubMemberRepository;
    private final AmazonS3 s3Client;
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    public void modifyClubMember(Long memberId, ClubMemberDto requestDto) {
        ClubMember clubMember = clubMemberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.CLUB_MEMBER_NOT_FOUND_ERROR));
        try{
            clubMember.setMemberName(requestDto.getMemberName());
            clubMember.setMemberIntro(requestDto.getMemberIntro());
            clubMember.setEmailPublic(requestDto.isEmailPublic());
            clubMember.setPhonePublic(requestDto.isPhonePublic());
            clubMemberRepository.save(clubMember);
        }catch(Exception e){
            throw new CommonException(ClubMemberErrorCode.CLUB_MEMBER_COULD_NOT_MODIFY_ERROR);
        }
    }
    // TDDO
    public MemberProfileDto getMemberProfile(Long memberId, Long userId) {

        ClubMember member = clubMemberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.CLUB_MEMBER_NOT_FOUND_ERROR));
        User user = userRepository.findById(userId)
                .orElseThrow(() ->new RuntimeException("User Not Found"));

        String storeFileName = member.getProfileImage().getStoreFileName();
        String memberProfileImage = fileUploadService.getFullPath(storeFileName);

        URL url = s3Client.getUrl(bucketName,memberId.toString());

        MemberProfileDto profile = MemberProfileDto.builder()
                .userName(user.getUserName())
                .userPhone(user.getUserPhone())
                .userEmail(user.getUserEmail())
                .memberName(member.getMemberName())
                .memberIntro(member.getMemberIntro())
                .role(member.getRole())
                .isMemberEmailPublic(member.isEmailPublic())
                .isMemberPhonePublic(member.isPhonePublic())
                .profileImage(url)
                .build();

        return profile;
    }

}