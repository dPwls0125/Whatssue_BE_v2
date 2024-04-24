package GDG.whatssue.domain.schedule.service;

import static GDG.whatssue.global.common.FileConst.DEFAULT_IMAGE_NAME;
import static GDG.whatssue.global.common.FileConst.MEMBER_PROFILE_IMAGE_DIRNAME;

import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.file.entity.UploadFile;
import GDG.whatssue.domain.file.service.FileUploadService;
import GDG.whatssue.domain.member.entity.ClubMember;
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
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ScheduleQueryRepository scheduleQueryRepository;
    private final FileUploadService fileUploadService;

    public void saveSchedule(Long clubId, Long memberId, AddScheduleRequest requestDto) {
        Club club = getClub(clubId);

        Schedule saveSchedule = requestDto.toEntity(club, clubMemberRepository.findById(memberId).get());
        scheduleRepository.save(saveSchedule);
    }

    public void updateSchedule(Long scheduleId, ModifyScheduleRequest requestDto) {
        Schedule schedule = getSchedule(scheduleId);
        schedule.update(requestDto);
    }

    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    public GetScheduleDetailResponse findSchedule(Long scheduleId) {
        Schedule schedule = getSchedule(scheduleId);

        return scheduleToGetScheduleDetailResponse(schedule);
    }

    public List<GetScheduleListResponse> findScheduleList(Long clubId, String sDate, String eDate) {
        List<Schedule> scheduleList = scheduleQueryRepository.findSchedules(clubId, sDate, eDate);
        return scheduleListToResponseDtoList(scheduleList);
    }

    public boolean isClubSchedule(Long clubId, Long scheduleId) {
        Schedule schedule = getSchedule(scheduleId);
        if (schedule.getClub().getId() == clubId) {
            return true;
        } else {
            return false;
        }
    }

    public List<GetScheduleListResponse> scheduleListToResponseDtoList(List<Schedule> scheduleList) {
        return scheduleList.stream()
            .map(s -> scheduleToGetScheduleListResponse(s))
            .collect(Collectors.toList());
    }

    public GetScheduleListResponse scheduleToGetScheduleListResponse(Schedule schedule) {
        return GetScheduleListResponse.builder()
            .scheduleId(schedule.getId())
            .scheduleName(schedule.getScheduleName())
            .scheduleDateTime(schedule.getScheduleDateTime())
            .attendanceStatus(schedule.getAttendanceStatus()).build();
    }

    public GetScheduleDetailResponse scheduleToGetScheduleDetailResponse(Schedule schedule) {
        ClubMember register = schedule.getClubMember();

        String registerProfileImage = fileUploadService.getFullPath(
                getStoreFileName(register.getProfileImage(), MEMBER_PROFILE_IMAGE_DIRNAME));

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

    private static String getStoreFileName(UploadFile uploadFile, String dirName) {
        String storeFileName;

        if (uploadFile != null) {
            storeFileName = uploadFile.getStoreFileName();
        } else {
            storeFileName = dirName + DEFAULT_IMAGE_NAME;
        }

        return storeFileName;
    }

    public Club getClub(Long clubId) {
        return clubRepository.findById(clubId)
            .orElseThrow(() -> new CommonException(ClubErrorCode.CLUB_NOT_FOUND_ERROR));
    }

    public Schedule getSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new CommonException(ScheduleErrorCode.SCHEDULE_NOT_FOUND_ERROR));
    }
}

