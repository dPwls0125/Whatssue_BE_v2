package GDG.whatssue.domain.schedule.service.impl;

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
import GDG.whatssue.domain.schedule.repository.ScheduleRepository;
import GDG.whatssue.domain.schedule.service.ScheduleService;
import GDG.whatssue.global.error.CommonException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final FileUploadService fileUploadService;

    @Override
    public void saveSchedule(Long clubId, Long memberId, AddScheduleRequest requestDto) {
        Club club = getClub(clubId);

        Schedule saveSchedule = requestDto.toEntity(club, clubMemberRepository.findById(memberId).get());
        scheduleRepository.save(saveSchedule);
    }

    @Override
    @Transactional
    public void updateSchedule(Long scheduleId, ModifyScheduleRequest requestDto) {
        Schedule schedule = getSchedule(scheduleId);

        schedule.update(requestDto);

        scheduleRepository.save(schedule);
    }

    @Override
    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    @Override
    public GetScheduleDetailResponse findSchedule(Long scheduleId) {
        Schedule schedule = getSchedule(scheduleId);

        return scheduleToGetScheduleDetailResponse(schedule);
    }

    @Override
    public List<GetScheduleListResponse> findScheduleAll(Long clubId) {
        Club findClub = getClub(clubId);

        List<Schedule> scheduleList = findClub.getScheduleList();

        return ScheduleListToResponseDtoList(scheduleList, null, null);
    }

    @Override
    public List<GetScheduleListResponse> findScheduleByDay(Long clubId, String date) {
        Club findClub = getClub(clubId);

        List<Schedule> scheduleList = findClub.getScheduleList();

        return ScheduleListToResponseDtoList(scheduleList, "yyyy-MM-dd", date);
    }
    @Override
    public List<GetScheduleListResponse> findScheduleByMonth(Long clubId, String date) {
        Club findClub = getClub(clubId);

        List<Schedule> scheduleList = findClub.getScheduleList();

        return ScheduleListToResponseDtoList(scheduleList, "yyyy-MM", date);
    }

    @Override
    public boolean isClubSchedule(Long clubId, Long scheduleId) {
        Schedule schedule = getSchedule(scheduleId);
        if (schedule.getClub().getId() == clubId) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 다중 조회랑 단일 조회 반환 dto 구분
     * 다중 조회 dto 새로 만들어야함 TODO
     */
    public List<GetScheduleListResponse> ScheduleListToResponseDtoList(List<Schedule> scheduleList, String pattern, String date) {

        if (pattern==null || date==null) { //전체 조회
            return scheduleList.stream()
                .map(s -> scheduleToGetScheduleListResponse(s))
                .collect(Collectors.toList());
        } else { //필터링 조회
        return scheduleList.stream()
            .filter(s-> s.getScheduleDateTime().format(DateTimeFormatter.ofPattern(pattern)).equals(date))
            .map(s -> scheduleToGetScheduleListResponse(s))
            .collect(Collectors.toList());
        }
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

