package GDG.whatssue.domain.schedule.service;

import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.schedule.dto.AddScheduleRequest;
import GDG.whatssue.domain.schedule.dto.AddScheduleResponse;
import GDG.whatssue.domain.schedule.dto.GetDateByScheduleExistResponse;
import GDG.whatssue.domain.schedule.dto.ScheduleDetailResponse;
import GDG.whatssue.domain.schedule.dto.ModifyScheduleRequest;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.schedule.dto.SchedulesResponse;
import GDG.whatssue.domain.schedule.entity.Schedule;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.schedule.exception.ScheduleErrorCode;
import GDG.whatssue.domain.schedule.repository.ScheduleRepository;
import GDG.whatssue.global.error.CommonException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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
        Schedule schedule = findSchedule(scheduleId, clubId);
        ClubMember register = schedule.getRegister();

        return new ScheduleDetailResponse(schedule, register);
    }

    public Page<SchedulesResponse> findAllSchedule(Long clubId, String keyword, LocalDate sDate, LocalDate eDate, Pageable pageable) {
        return scheduleRepository.findAllScheduleDto(clubId, keyword, sDate, eDate, pageable);
    }

    public Page<LocalDate> getDateByScheduleExist(Long clubId, LocalDate sDate, LocalDate eDate, Pageable pageable) {
        LocalDateTime sDateTime = sDate.atStartOfDay();
        LocalDateTime eDateTime = LocalDateTime.of(eDate, LocalTime.MAX).withNano(0);

        return scheduleRepository.findDateByScheduleExist(clubId, sDateTime, eDateTime, pageable)
            .map(d -> d.toLocalDate());
    }

    public boolean isClubSchedule(Long clubId, Long scheduleId) {
        return scheduleRepository.existsByIdAndClub_Id(scheduleId, clubId);
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

