package GDG.whatssue.domain.officialabsence.service;

import GDG.whatssue.domain.attendance.repository.ScheduleAttendanceResultRepository;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.officialabsence.dto.OfficialAbsenceAddRequestDto;
import GDG.whatssue.domain.officialabsence.dto.OfficialAbsenceGetRequestDto;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequestType;
import GDG.whatssue.domain.officialabsence.exception.OfficialAbsenceErrorCode;
import GDG.whatssue.domain.officialabsence.repository.OfficialAbsenceRequestRepository;
import GDG.whatssue.domain.post.dto.GetPostResponse;
import GDG.whatssue.domain.schedule.entity.Schedule;
import GDG.whatssue.domain.schedule.exception.ScheduleErrorCode;
import GDG.whatssue.domain.schedule.repository.ScheduleRepository;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import GDG.whatssue.global.error.CommonException;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OfficialAbsenceService {
    private final ScheduleRepository scheduleRepository;
    private final OfficialAbsenceRequestRepository officialAbsenceRequestRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ScheduleAttendanceResultRepository scheduleAttendanceResultRepository;
    private static final Logger logger = LoggerFactory.getLogger(OfficialAbsenceService.class);

    @Transactional
    public void createOfficialAbsenceRequest(Long userId, Long clubId, Long scheduleId, String officialAbsenceContent) {
        //공결 신청 생성
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CommonException(ScheduleErrorCode.EX4100)); //존재하지 않는 일정

        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId)
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100));//존재하지 않는 멤버

        if (officialAbsenceRequestRepository.existsByScheduleAndClubMember(schedule, clubMember)) {
            throw new CommonException(OfficialAbsenceErrorCode.EX6200); //중복 신청
        }

        OfficialAbsenceRequest officialAbsenceRequest = OfficialAbsenceRequest.builder()
            .clubMember(clubMember)
            .schedule(schedule)
            .officialAbsenceContent(officialAbsenceContent)
            .officialAbsenceRequestType(OfficialAbsenceRequestType.WAITING)
            .build();
        officialAbsenceRequestRepository.save(officialAbsenceRequest);
    }
    public Page<OfficialAbsenceGetRequestDto> getMyOfficialAbsenceRequests(Long userId, Long clubId, Pageable pageable) {
        //내 공결 신청 List 조회
        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId)
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100)); // 존재하지 않는 멤버

        Page<OfficialAbsenceRequest> officialAbsenceRequests = officialAbsenceRequestRepository.findByClubMember(clubMember, pageable);

        List<OfficialAbsenceGetRequestDto> officialAbsenceGetRequestDtos = new ArrayList<>();

        for(OfficialAbsenceRequest officialAbsenceRequest : officialAbsenceRequests){
            OfficialAbsenceGetRequestDto response = OfficialAbsenceGetRequestDto.builder()
                    .id(officialAbsenceRequest.getId())
                    .clubMemberId(officialAbsenceRequest.getClubMember().getId())
                    .scheduleId(officialAbsenceRequest.getSchedule().getId())
                    .officialAbsenceContent((officialAbsenceRequest.getOfficialAbsenceContent()))
                    .officialAbsenceRequestType(officialAbsenceRequest.getOfficialAbsenceRequestType())
                    .createdAt((officialAbsenceRequest.getCreateAt()))
                    .build();

            officialAbsenceGetRequestDtos.add(response);
        }
        return new PageImpl<>(officialAbsenceGetRequestDtos, pageable, officialAbsenceRequests.getTotalElements());
    }
    @Transactional
    public void deleteOfficialAbsences(Long userId, Long clubId, Long officialAbsenceId) {
        //내 공결 신청 취소
        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId)
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100)); // 존재하지 않는 멤버

        OfficialAbsenceRequest officialAbsenceRequest = officialAbsenceRequestRepository.findById(officialAbsenceId).get();

        if(officialAbsenceRequest == null){
            throw new CommonException(OfficialAbsenceErrorCode.EX6100); //존재하지 않는 일정
        }
        else if(officialAbsenceRequest.getClubMember().getId()!=clubMember.getId()){
            throw new CommonException(OfficialAbsenceErrorCode.EX6201); //본인만 공결 신청 취소 가능
        }
        officialAbsenceRequestRepository.delete(officialAbsenceRequest);
    }

    public Page<OfficialAbsenceGetRequestDto> getAllOfficialAbsenceRequests(Long clubId, Pageable pageable) {
        //전체 공결 신청 조회(MANAGER)

        Page<OfficialAbsenceRequest> officialAbsenceRequests = officialAbsenceRequestRepository.findByClubMember_Club_Id(clubId, pageable);

        List<OfficialAbsenceGetRequestDto> officialAbsenceGetRequestDtos = new ArrayList<>();


        for(OfficialAbsenceRequest officialAbsenceRequest : officialAbsenceRequests){
            OfficialAbsenceGetRequestDto response = OfficialAbsenceGetRequestDto.builder()
                    .id(officialAbsenceRequest.getId())
                    .clubMemberId(officialAbsenceRequest.getClubMember().getId())
                    .scheduleId(officialAbsenceRequest.getSchedule().getId())
                    .officialAbsenceContent((officialAbsenceRequest.getOfficialAbsenceContent()))
                    .officialAbsenceRequestType(officialAbsenceRequest.getOfficialAbsenceRequestType())
                    .createdAt((officialAbsenceRequest.getCreateAt()))
                    .build();

            officialAbsenceGetRequestDtos.add(response);
        }

        return new PageImpl<>(officialAbsenceGetRequestDtos, pageable, officialAbsenceRequests.getTotalElements());
    }
    public Page<OfficialAbsenceGetRequestDto> getWaitingOfficialAbsenceRequests(Long clubId, Pageable pageable) {
        //대기중인 공결 신청 조회(MANAGER)
        //Request_Type WAITING 필터링 (수락 대기중)
        Page<OfficialAbsenceRequest> officialAbsenceRequests = officialAbsenceRequestRepository.findByClubMember_Club_IdAndOfficialAbsenceRequestType(clubId, OfficialAbsenceRequestType.WAITING, pageable);

        List<OfficialAbsenceGetRequestDto> officialAbsenceGetRequestDtos = new ArrayList<>();

        for(OfficialAbsenceRequest officialAbsenceRequest : officialAbsenceRequests){
            OfficialAbsenceGetRequestDto response = OfficialAbsenceGetRequestDto.builder()
                    .id(officialAbsenceRequest.getId())
                    .clubMemberId(officialAbsenceRequest.getClubMember().getId())
                    .scheduleId(officialAbsenceRequest.getSchedule().getId())
                    .officialAbsenceContent((officialAbsenceRequest.getOfficialAbsenceContent()))
                    .officialAbsenceRequestType(officialAbsenceRequest.getOfficialAbsenceRequestType())
                    .createdAt((officialAbsenceRequest.getCreateAt()))
                    .build();

            officialAbsenceGetRequestDtos.add(response);
        }

        return new PageImpl<>(officialAbsenceGetRequestDtos, pageable, officialAbsenceRequests.getTotalElements());
    }
    public Page<OfficialAbsenceGetRequestDto> getDoneOfficialAbsenceRequests(Long clubId, Pageable pageable) {
        //과거 공결 신청 내역 조회(MANAGER)
        //Request_TypeNot WAITING 필터링 (수락 or 거절 완료)
        Page<OfficialAbsenceRequest> officialAbsenceRequests = officialAbsenceRequestRepository.findByClubMember_Club_IdAndOfficialAbsenceRequestTypeNot(clubId, OfficialAbsenceRequestType.WAITING, pageable);

        List<OfficialAbsenceGetRequestDto> officialAbsenceGetRequestDtos = new ArrayList<>();

        for(OfficialAbsenceRequest officialAbsenceRequest : officialAbsenceRequests){
            OfficialAbsenceGetRequestDto response = OfficialAbsenceGetRequestDto.builder()
                    .id(officialAbsenceRequest.getId())
                    .clubMemberId(officialAbsenceRequest.getClubMember().getId())
                    .scheduleId(officialAbsenceRequest.getSchedule().getId())
                    .officialAbsenceContent((officialAbsenceRequest.getOfficialAbsenceContent()))
                    .officialAbsenceRequestType(officialAbsenceRequest.getOfficialAbsenceRequestType())
                    .createdAt((officialAbsenceRequest.getCreateAt()))
                    .build();

            officialAbsenceGetRequestDtos.add(response);
        }

        return new PageImpl<>(officialAbsenceGetRequestDtos, pageable, officialAbsenceRequests.getTotalElements());
    }

    public OfficialAbsenceGetRequestDto getOfficialAbsenceRequestDetail(Long clubId, Long officialAbsenceId){
        //특정 단일 공결 신청 조회(MANAGER)
        OfficialAbsenceRequest officialAbsenceRequest = officialAbsenceRequestRepository.findByClubMember_Club_IdAndId(clubId, officialAbsenceId)
                .orElseThrow(() -> new CommonException(OfficialAbsenceErrorCode.EX6100)); //존재하지 않는 공결신청

        OfficialAbsenceGetRequestDto officialAbsenceGetRequestDto = OfficialAbsenceGetRequestDto.builder()
                .id(officialAbsenceRequest.getId())
                .clubMemberId(officialAbsenceRequest.getClubMember().getId())
                .scheduleId(officialAbsenceRequest.getSchedule().getId())
                .officialAbsenceContent(officialAbsenceRequest.getOfficialAbsenceContent())
                .officialAbsenceRequestType(officialAbsenceRequest.getOfficialAbsenceRequestType())
                .build();

        return officialAbsenceGetRequestDto;
    }

    @Transactional
    public void acceptResponse(Long clubId, Long officialAbsenceId) {
        // 공결 신청 수락(MANAGER)
        OfficialAbsenceRequest officialAbsenceRequest = officialAbsenceRequestRepository.findByClubMember_Club_IdAndId(clubId, officialAbsenceId)
                .orElseThrow(() -> new CommonException(OfficialAbsenceErrorCode.EX6100)); //존재하지 않는 공결신청

        officialAbsenceRequest.setOfficialAbsenceRequestType(OfficialAbsenceRequestType.ACCEPTED);
    }
    @Transactional
    public void denyResponse(Long clubId,Long officialAbsenceId) {
        // 공결 신청 거절(MANAGER)
        OfficialAbsenceRequest officialAbsenceRequest = officialAbsenceRequestRepository.findByClubMember_Club_IdAndId(clubId, officialAbsenceId)
                .orElseThrow(() -> new CommonException(OfficialAbsenceErrorCode.EX6100)); //존재하지 않는 공결신청

        officialAbsenceRequest.setOfficialAbsenceRequestType(OfficialAbsenceRequestType.REJECTED);
    }
}