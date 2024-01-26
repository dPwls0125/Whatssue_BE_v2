package GDG.whatssue.service;

import GDG.whatssue.dto.OfficialAbsence.OfficialAbsenceAddRequestDto;
import GDG.whatssue.dto.OfficialAbsence.OfficialAbsenceGetRequestDto;
import GDG.whatssue.entity.ClubMember;
import GDG.whatssue.entity.OfficialAbsenceRequest;
import GDG.whatssue.entity.Schedule;
import GDG.whatssue.entity.ScheduleAttendanceResult;
import GDG.whatssue.repository.ClubMemberRepository;
import GDG.whatssue.repository.OfficialAbsenceRequestRepository;
import GDG.whatssue.repository.ScheduleAttendanceResultRepository;
import GDG.whatssue.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

import static GDG.whatssue.entity.AttendanceType.ABSENCE;
import static GDG.whatssue.entity.AttendanceType.OFFICIAL_ABSENCE;

@Service
@RequiredArgsConstructor
public class OfficialAbsenceService {
    private final ScheduleRepository scheduleRepository;
    private final OfficialAbsenceRequestRepository officialAbsenceRequestRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ScheduleAttendanceResultRepository scheduleAttendanceResultRepository;

    public void createOfficialAbsenceRequest(Long scheduleId, OfficialAbsenceAddRequestDto officialAbsenceAddRequestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        ClubMember clubMember = clubMemberRepository.findById(officialAbsenceAddRequestDto.getClubMemberId()).get();
        String officialAbsenceContent = officialAbsenceAddRequestDto.getOfficialAbsenceContent();

        ClubMember clubMemberEntity = clubMemberRepository.findById(clubMember.getId()).get();

        OfficialAbsenceRequest officialAbsenceRequest = OfficialAbsenceRequest.builder()
                .clubMember(clubMemberEntity)
                .schedule(schedule)
                .officialAbsenceContent(officialAbsenceContent)
                .build();

        officialAbsenceRequestRepository.save(officialAbsenceRequest);

    }
    public List<OfficialAbsenceGetRequestDto> getOfficialAbsenceRequests() { //공결 신청 List 조회
        List<OfficialAbsenceRequest> officialAbsenceRequests = officialAbsenceRequestRepository.findAll();

        return officialAbsenceRequests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private OfficialAbsenceGetRequestDto convertToDto(OfficialAbsenceRequest officialAbsenceRequest) {
        return OfficialAbsenceGetRequestDto.builder()
                .id(officialAbsenceRequest.getId())
                .clubMemberId(officialAbsenceRequest.getClubMember().getId())
                .scheduleId(officialAbsenceRequest.getSchedule().getId())
                .officialAbsenceContent(officialAbsenceRequest.getOfficialAbsenceContent())
                .build();
    }
    public void acceptResponse(Long officialAbsenceId){ // 공결 신청 수락
        OfficialAbsenceRequest officialAbsenceRequest = officialAbsenceRequestRepository.findById(officialAbsenceId).get();
        Long scheduleId = scheduleRepository.findById(officialAbsenceRequest.getId()).get().getId();
        Long clubMemberId = clubMemberRepository.findById(officialAbsenceRequest.getId()).get().getId();

        ScheduleAttendanceResult scheduleAttendanceResult = scheduleAttendanceResultRepository.findByScheduleIdAndClubMemberId(scheduleId,clubMemberId);

        scheduleAttendanceResult.setAttendanceType(OFFICIAL_ABSENCE);
    }
    public void denyResponse(Long officialAbsenceId){ // 공결 신청 거절
        OfficialAbsenceRequest officialAbsenceRequest = officialAbsenceRequestRepository.findById(officialAbsenceId).get();
        Long scheduleId = scheduleRepository.findById(officialAbsenceRequest.getId()).get().getId();
        Long clubMemberId = clubMemberRepository.findById(officialAbsenceRequest.getId()).get().getId();

        ScheduleAttendanceResult scheduleAttendanceResult = scheduleAttendanceResultRepository.findByScheduleIdAndClubMemberId(scheduleId,clubMemberId);

        scheduleAttendanceResult.setAttendanceType(ABSENCE);
    }
}
