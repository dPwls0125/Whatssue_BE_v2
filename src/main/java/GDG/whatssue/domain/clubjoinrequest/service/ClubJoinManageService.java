package GDG.whatssue.domain.clubjoinrequest.service;

import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.clubjoinrequest.entity.ClubJoinRequest;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.clubjoinrequest.repository.ClubJoinRequestRepository;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.clubjoinrequest.dto.ClubJoinRequestGetDto;
import GDG.whatssue.global.error.CommonException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.tool.schema.spi.CommandAcceptanceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubJoinManageService {
    private final ClubJoinRequestRepository clubJoinRequestRepository;
    private final ClubMemberRepository clubMemberRepository;

    @Transactional
    public void acceptResponse(List<Long> clubJoinRequestsId,Long clubId) {//수락
        // 클럽 가입 요청 엔티티 조회
        for (Long clubJoinRequestId : clubJoinRequestsId) {
            ClubJoinRequest clubJoinRequest = clubJoinRequestRepository.findById(clubJoinRequestId)
                    .orElseThrow(() -> new CommonException(ClubErrorCode.EX3102)); //존재하지 않는 클럽 가입 신청

            if(clubJoinRequest.getClub().getId()!=clubId){
                throw new CommonException(ClubErrorCode.EX3207); // 다른 클럽의 가입 신청에 접근 불가능
            }

            if(clubMemberRepository.findByClub_IdAndUser_UserId(clubId,clubJoinRequest.getUser().getUserId()).isPresent()) {
                throw new CommonException(ClubErrorCode.EX3200); // 이미 가입한 모임
            }
            ClubMember member = ClubMember.newMember(clubJoinRequest.getClub(), clubJoinRequest.getUser());

            clubMemberRepository.save(member);
            clubJoinRequestRepository.delete(clubJoinRequest);
        }
    }
    @Transactional
    public void denyResponse(List<Long> clubJoinRequestsId, Long clubId) {//거절
        // 클럽 가입 요청 엔티티 조회
        for (Long clubJoinRequestId : clubJoinRequestsId) {
            ClubJoinRequest clubJoinRequest = clubJoinRequestRepository.findById(clubJoinRequestId)
                    .orElseThrow(() -> new CommonException(ClubErrorCode.EX3102)); //존재하지 않는 클럽 가입 신청

            if(clubJoinRequest.getClub().getId()!=clubId){
                throw new CommonException(ClubErrorCode.EX3207); // 다른 클럽의 가입 신청에 접근 불가능
            }

            if(clubMemberRepository.findByClub_IdAndUser_UserId(clubId,clubJoinRequest.getUser().getUserId()).isPresent()) {
                throw new CommonException(ClubErrorCode.EX3200); // 이미 가입한 모임
            }
            clubJoinRequestRepository.delete(clubJoinRequest);
        }
    }

    @Transactional
    public List<ClubJoinRequestGetDto> getClubJoinRequests(Long clubId) {
        List<ClubJoinRequest> clubJoinRequests = clubJoinRequestRepository.findByClub_Id(clubId);

        // Dto 매핑
        List<ClubJoinRequestGetDto> dtos = clubJoinRequests.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return dtos;
    }

    // Dto 매핑
    private ClubJoinRequestGetDto mapToDto(ClubJoinRequest clubJoinRequest) {
        return ClubJoinRequestGetDto.builder()
                .id(clubJoinRequest.getId())
                .clubId(clubJoinRequest.getClub().getId())
                .userId(clubJoinRequest.getUser().getUserId())
                .userName(clubJoinRequest.getUser().getUserName())
                .createdAt(clubJoinRequest.getCreateAt())
                .build();
    }

    @Transactional
    public void deleteMember(Long clubId, Long userId) {//멤버 삭제
        // 해당 클럽 ID와 사용자 ID에 해당하는 클럽 멤버를 조회합니다.
        Optional<ClubMember> clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId);
            clubMemberRepository.delete(clubMember.get());
    }
}