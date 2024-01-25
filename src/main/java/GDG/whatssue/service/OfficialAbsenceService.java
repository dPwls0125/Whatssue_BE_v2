package GDG.whatssue.service;

import GDG.whatssue.dto.OfficialAbsence.OfficialAbsenceAddRequestDto;
import GDG.whatssue.dto.OfficialAbsence.OfficialAbsenceGetRequestDto;
import GDG.whatssue.entity.ClubMember;
import GDG.whatssue.entity.OfficialAbsenceRequest;
import GDG.whatssue.entity.Schedule;
import GDG.whatssue.repository.ClubMemberRepository;
import GDG.whatssue.repository.OfficialAbsenceRequestRepository;
import GDG.whatssue.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfficialAbsenceService {
    private final ScheduleRepository scheduleRepository;
    private final OfficialAbsenceRequestRepository officialAbsenceRequestRepository;
    private final ClubMemberRepository clubMemberRepository;

    public void createOfficialAbsenceRequest(Long scheduleId, OfficialAbsenceAddRequestDto officialAbsenceAddRequestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        ClubMember clubMember = officialAbsenceAddRequestDto.getClubMember();
        String officialAbsenceContent = officialAbsenceAddRequestDto.getOfficialAbsenceContent();

        ClubMember clubMemberEntity = clubMemberRepository.findById(clubMember.getId()).get();

        OfficialAbsenceRequest officialAbsenceRequest = OfficialAbsenceRequest.builder()
                .clubMember(clubMemberEntity)
                .schedule(schedule)
                .officialAbsenceContent(officialAbsenceContent)
                .build();

        officialAbsenceRequestRepository.save(officialAbsenceRequest);

    }
    public List<OfficialAbsenceGetRequestDto> getOfficialAbsenceRequests() {
        List<OfficialAbsenceRequest> officialAbsenceRequests = officialAbsenceRequestRepository.findAll();

        return officialAbsenceRequests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private OfficialAbsenceGetRequestDto convertToDto(OfficialAbsenceRequest officialAbsenceRequest) {
        return OfficialAbsenceGetRequestDto.builder()
                .id(officialAbsenceRequest.getId())
                .clubMember(officialAbsenceRequest.getClubMember())
                .schedule(officialAbsenceRequest.getSchedule())
                .officialAbsenceContent(officialAbsenceRequest.getOfficialAbsenceContent())
                .build();
    }
}
