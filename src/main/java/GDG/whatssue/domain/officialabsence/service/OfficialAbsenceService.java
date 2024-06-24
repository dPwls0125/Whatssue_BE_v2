package GDG.whatssue.domain.officialabsence.service;

import GDG.whatssue.domain.attendance.service.repository.ScheduleAttendanceResultRepository;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.officialabsence.dto.OfficialAbsenceAddRequestDto;
import GDG.whatssue.domain.officialabsence.dto.OfficialAbsenceGetRequestDto;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequestType;
import GDG.whatssue.domain.officialabsence.exception.OfficialAbsenceErrorCode;
import GDG.whatssue.domain.officialabsence.repository.OfficialAbsenceRequestRepository;
import GDG.whatssue.domain.schedule.entity.Schedule;
import GDG.whatssue.domain.schedule.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public void createOfficialAbsenceRequest(Long scheduleId, OfficialAbsenceAddRequestDto officialAbsenceAddRequestDto) { //공결 신청 생성
        //예외처리
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CommonException(OfficialAbsenceErrorCode.EX6100)); //존재하지 않는 일정

        ClubMember clubMember = clubMemberRepository.findById(officialAbsenceAddRequestDto.getClubMemberId())
                .orElseThrow(() -> new CommonException(OfficialAbsenceErrorCode.EX6101)); //존재하지 않는 멤버

        boolean isAlreadyRequested = officialAbsenceRequestRepository.existsByScheduleAndClubMember(schedule, clubMember);
        if (isAlreadyRequested) {
            throw new CommonException(OfficialAbsenceErrorCode.EX6200); //중복 신청
        }

        String officialAbsenceContent = officialAbsenceAddRequestDto.getOfficialAbsenceContent();
        ClubMember clubMemberEntity = clubMemberRepository.findById(clubMember.getId()).get();

        OfficialAbsenceRequest officialAbsenceRequest = OfficialAbsenceRequest.builder()
            .clubMember(clubMemberEntity)
            .schedule(schedule)
            .officialAbsenceContent(officialAbsenceContent)
            .officialAbsenceRequestType(OfficialAbsenceRequestType.WAITING)
            .build();

        officialAbsenceRequestRepository.save(officialAbsenceRequest);

    }

    public List<OfficialAbsenceGetRequestDto> getAllOfficialAbsenceRequests() { //공결 신청 현황, 내역 List 조회
        //모든 현황
        List<OfficialAbsenceRequest> officialAbsenceRequests = officialAbsenceRequestRepository.findAll();
        return officialAbsenceRequests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public List<OfficialAbsenceGetRequestDto> getOfficialAbsenceRequests() { //공결 신청 현황 List 조회
        List<OfficialAbsenceRequest> officialAbsenceRequests = officialAbsenceRequestRepository.findByOfficialAbsenceRequestType(OfficialAbsenceRequestType.WAITING);
        //Request_Type WAITING 필터링 (수락 대기중)
        return officialAbsenceRequests.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    public List<OfficialAbsenceGetRequestDto> getDoneOfficialAbsenceRequests() { //공결 신청 내역 List 조회
        //Request_Type WAITING 필터링 (수락 or 거절 완료)
        List<OfficialAbsenceRequest> officialAbsenceRequests = officialAbsenceRequestRepository.findByOfficialAbsenceRequestTypeNot(OfficialAbsenceRequestType.WAITING);
        return officialAbsenceRequests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public OfficialAbsenceGetRequestDto getOfficialAbsenceRequestDetail(Long officialAbsenceId){
        //특정 단일 공결 신청 조회
        OfficialAbsenceRequest officialAbsenceRequest = officialAbsenceRequestRepository.findById(officialAbsenceId)
                .orElseThrow(() -> new CommonException(OfficialAbsenceErrorCode.EX6102)); //존재하지 않는 공결신청

        OfficialAbsenceGetRequestDto officialAbsenceGetRequestDto = OfficialAbsenceGetRequestDto.builder()
                .id(officialAbsenceRequest.getId())
                .clubMemberId(officialAbsenceRequest.getClubMember().getId())
                .scheduleId(officialAbsenceRequest.getSchedule().getId())
                .officialAbsenceContent(officialAbsenceRequest.getOfficialAbsenceContent())
                .officialAbsenceRequestType(officialAbsenceRequest.getOfficialAbsenceRequestType())
                .build();

        return officialAbsenceGetRequestDto;
    }

    public List<OfficialAbsenceGetRequestDto> getMyDoneOfficialAbsenceRequests(Long clubMemberId) { //내 공결 신청 내역 List 조회
        // clubMemberId 유효성 검사
        if (clubMemberId == null || clubMemberId <= 0) {
            throw new CommonException(OfficialAbsenceErrorCode.EX6101); //존재하지 않는 멤버
        }
        //isChecked true 필터링(수락 승인 or 거절 완료)
        List<OfficialAbsenceRequest> officialAbsenceRequests = officialAbsenceRequestRepository.findByOfficialAbsenceRequestTypeNot(OfficialAbsenceRequestType.WAITING);
        // 공결 신청 내역이 없는 경우
        if (officialAbsenceRequests.isEmpty()) {
            throw new CommonException(OfficialAbsenceErrorCode.EX6103); //공결 신청 내역이 존재하지 않음
        }
        return officialAbsenceRequests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public List<OfficialAbsenceGetRequestDto> getMyOfficialAbsenceRequests(Long clubMemberId) { //내 공결 신청 내역 List 조회
        // clubMemberId 유효성 검사
        if (clubMemberId == null || clubMemberId <= 0) {
            throw new CommonException(OfficialAbsenceErrorCode.EX6101); //존재하지 않는 멤버
        }
        List<OfficialAbsenceRequest> officialAbsenceRequests = officialAbsenceRequestRepository.findByOfficialAbsenceRequestTypeNot(OfficialAbsenceRequestType.WAITING);
        // 공결 신청 내역이 없는 경우
        if (officialAbsenceRequests.isEmpty()) {
            throw new CommonException(OfficialAbsenceErrorCode.EX6103); //공결 신청 내역이 존재하지 않음
        }
        return officialAbsenceRequests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    private OfficialAbsenceGetRequestDto convertToDto(OfficialAbsenceRequest officialAbsenceRequest) {
        /**isChecked DTO에 필요해? 확인 필요**/
        return OfficialAbsenceGetRequestDto.builder()
            .id(officialAbsenceRequest.getId())
            .clubMemberId(officialAbsenceRequest.getClubMember().getId())
            .scheduleId(officialAbsenceRequest.getSchedule().getId())
            .officialAbsenceContent(officialAbsenceRequest.getOfficialAbsenceContent())
            .officialAbsenceRequestType(officialAbsenceRequest.getOfficialAbsenceRequestType())
            .build();
    }
    @Transactional
    public void deleteOfficialAbsences(Long officialAbsenceId,Long clubMemberId) { //내 공결 신청 취소
        // clubMemberId 유효성 검사
        if (clubMemberId == null || clubMemberId <= 0) {
            throw new CommonException(OfficialAbsenceErrorCode.EX6101); //존재하지 않는 멤버
        }
        OfficialAbsenceRequest officialAbsenceRequest = officialAbsenceRequestRepository.findById(officialAbsenceId).get();
        if(officialAbsenceRequest == null){
            throw new CommonException(OfficialAbsenceErrorCode.EX6100); //존재하지 않는 일정
        }
        officialAbsenceRequestRepository.delete(officialAbsenceRequest);
    }


    @Transactional
    public void acceptResponse(Long officialAbsenceId) {
        try {
            OfficialAbsenceRequest officialAbsenceRequest = officialAbsenceRequestRepository.findById(officialAbsenceId)
                .orElseThrow(() -> new CommonException(OfficialAbsenceErrorCode.EX6100)); //존재하지 않는 일정

            officialAbsenceRequest.setOfficialAbsenceRequestType(OfficialAbsenceRequestType.ACCEPTED);
        } catch (NoSuchElementException e) {
            logger.warn("Error processing acceptResponse: {}", e.getMessage());
        }
    }
    @Transactional
    public void denyResponse(Long officialAbsenceId) { // 공결 신청 거절
        try {
            OfficialAbsenceRequest officialAbsenceRequest = officialAbsenceRequestRepository.findById(officialAbsenceId)
                .orElseThrow(() -> new CommonException(OfficialAbsenceErrorCode.EX6100)); //존재하지 않는 일정

            officialAbsenceRequest.setOfficialAbsenceRequestType(OfficialAbsenceRequestType.REJECTED);
        } catch (NoSuchElementException e) {
            logger.warn("Error processing acceptResponse: {}", e.getMessage());
        }
    }
}