package GDG.whatssue.domain.schedule.service;

import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.file.service.FileUploadService;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.schedule.dto.AddScheduleRequest;
import GDG.whatssue.domain.schedule.dto.GetScheduleDetailResponse;
import GDG.whatssue.domain.schedule.dto.GetScheduleListResponse;
import GDG.whatssue.domain.schedule.dto.ModifyScheduleRequest;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.schedule.entity.Schedule;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.schedule.exception.ScheduleErrorCode;
import GDG.whatssue.domain.schedule.repository.ScheduleQueryRepository;
import GDG.whatssue.domain.schedule.repository.ScheduleRepository;
import GDG.whatssue.global.error.CommonException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ScheduleQueryRepository scheduleQueryRepository;
    private final FileUploadService fileUploadService;

    @Transactional
    public void saveSchedule(Long clubId, Long memberId, AddScheduleRequest requestDto) {
        Schedule schedule = requestDto.toEntity(findClub(clubId), findMember(memberId));
        scheduleRepository.save(schedule);
    }

    @Transactional
    public void updateSchedule(Long scheduleId, ModifyScheduleRequest requestDto) {
        Schedule schedule = findSchedule(scheduleId);
        schedule.update(requestDto);
    }

    @Transactional
    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    public GetScheduleDetailResponse findScheduleById(Long scheduleId) {
        return scheduleToGetScheduleDetailResponse(findSchedule(scheduleId));
    }

    public List<GetScheduleListResponse> findScheduleByFilter(Long clubId, String query, String sDate, String eDate) {
        List<Schedule> scheduleList = scheduleQueryRepository.findScheduleByFilter(clubId, query, sDate, eDate);
        return scheduleListToResponseDtoList(scheduleList);
    }

    public boolean isClubSchedule(Long clubId, Long scheduleId) {
        return scheduleRepository.existsByIdAndClub_Id(scheduleId, clubId);
    }

    private List<GetScheduleListResponse> scheduleListToResponseDtoList(List<Schedule> scheduleList) {
        return scheduleList.stream()
            .map(s -> scheduleToGetScheduleListResponse(s))
            .collect(Collectors.toList());
    }

    private GetScheduleListResponse scheduleToGetScheduleListResponse(Schedule schedule) {
        return GetScheduleListResponse.builder()
            .scheduleId(schedule.getId())
            .scheduleName(schedule.getScheduleName())
            .scheduleDateTime(schedule.getScheduleDateTime())
            .attendanceStatus(schedule.getAttendanceStatus()).build();
    }

    private GetScheduleDetailResponse scheduleToGetScheduleDetailResponse(Schedule schedule) {
        ClubMember register = schedule.getRegister();

        String storeFileName = register.getProfileImage().getStoreFileName();
        String registerProfileImage = fileUploadService.getFullPath(storeFileName);

        return GetScheduleDetailResponse.builder()
            .scheduleId(schedule.getId())
            .scheduleName(schedule.getScheduleName())
            .scheduleContent(schedule.getScheduleContent())
            .scheduleDateTime(schedule.getScheduleDateTime())
            .schedulePlace(schedule.getSchedulePlace())
            .registerName(register.getMemberName())
            .registerProfileImage(registerProfileImage)
            .registerTime(schedule.getCreateAt())
            .attendanceStatus(schedule.getAttendanceStatus()).build();
    }

    private Club findClub(Long clubId) {
        return clubRepository.findById(clubId)
            .orElseThrow(() -> new CommonException(ClubErrorCode.CLUB_NOT_FOUND_ERROR));
    }

    private ClubMember findMember(Long memberId) {
        return clubMemberRepository.findById(memberId)
            .orElseThrow(() -> new CommonException(ClubMemberErrorCode.CLUB_MEMBER_NOT_FOUND_ERROR));
    }

    private Schedule findSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new CommonException(ScheduleErrorCode.SCHEDULE_NOT_FOUND_ERROR));
    }
}

