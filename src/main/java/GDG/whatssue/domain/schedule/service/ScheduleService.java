package GDG.whatssue.domain.schedule.service;

import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.schedule.dto.AddScheduleRequest;
import GDG.whatssue.domain.schedule.dto.AddScheduleResponse;
import GDG.whatssue.domain.schedule.dto.ScheduleDetailResponse;
import GDG.whatssue.domain.schedule.dto.ModifyScheduleRequest;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.schedule.dto.SchedulesResponse;
import GDG.whatssue.domain.schedule.entity.Schedule;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.schedule.exception.ScheduleErrorCode;
import GDG.whatssue.domain.schedule.repository.ScheduleRepository;
import GDG.whatssue.global.util.S3Utils;
import GDG.whatssue.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;

    @Transactional
    public AddScheduleResponse saveSchedule(Long clubId, Long userId, AddScheduleRequest requestDto) {
        Schedule schedule = requestDto.toEntity(findClub(clubId), findMember(clubId, userId));

        scheduleRepository.save(schedule);

        return new AddScheduleResponse(schedule.getId());
    }

    @Transactional
    public void updateSchedule(Long clubId, Long scheduleId, ModifyScheduleRequest requestDto) {
        findSchedule(scheduleId, clubId).update(
            requestDto.getScheduleName(),
            requestDto.getScheduleContent(),
            requestDto.getScheduleDate(),
            requestDto.getScheduleTime(),
            requestDto.getSchedulePlace());
    }

    @Transactional
    public void deleteSchedule(Long clubId, Long scheduleId) {
        scheduleRepository.delete(findSchedule(clubId, scheduleId));
    }

    public ScheduleDetailResponse getScheduleDetail(Long clubId, Long scheduleId) {
        return scheduleToGetScheduleDetailResponse(findSchedule(scheduleId, clubId));
    }

    public Page<SchedulesResponse> findAllSchedule(Long clubId, String query, String sDate, String eDate, Pageable pageable) {
        return scheduleRepository.findAllSchedule(clubId, query, sDate, eDate, pageable);
    }

    public boolean isClubSchedule(Long clubId, Long scheduleId) {
        return scheduleRepository.existsByIdAndClub_Id(scheduleId, clubId);
    }

    private ScheduleDetailResponse scheduleToGetScheduleDetailResponse(Schedule schedule) {
        ClubMember register = schedule.getRegister();

        String storeFileName = register.getProfileImage().getStoreFileName();
        String registerProfileImage = S3Utils.getFullPath(storeFileName);

        return ScheduleDetailResponse.builder()
            .scheduleId(schedule.getId())
            .scheduleName(schedule.getScheduleName())
            .scheduleContent(schedule.getScheduleContent())
            .scheduleDate(schedule.getScheduleDate())
            .scheduleTime(schedule.getScheduleTime())
            .schedulePlace(schedule.getSchedulePlace())
            .registerName(register.getMemberName())
            .registerProfileImage(registerProfileImage)
            .registerTime(schedule.getCreateAt())
            .attendanceStatus(schedule.getAttendanceStatus()).build();
    }

    private Club findClub(Long clubId) {
        return clubRepository.findById(clubId)
            .orElseThrow(() -> new CommonException(ClubErrorCode.EX3100));
    }

    private ClubMember findMember(Long clubId, Long userId) {
        return clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId)
            .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100));
    }

    private Schedule findSchedule(Long scheduleId, Long clubId) {
        return scheduleRepository.findByIdAndClub_Id(scheduleId, clubId)
            .orElseThrow(() -> new CommonException(ScheduleErrorCode.EX4100));
    }
}

