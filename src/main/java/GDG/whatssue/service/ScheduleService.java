package GDG.whatssue.service;

import GDG.whatssue.dto.schedule.AddScheduleRequestDto;
import GDG.whatssue.dto.schedule.GetScheduleResponseDto;
import GDG.whatssue.dto.schedule.ModifyScheduleRequestDto;
import GDG.whatssue.entity.Club;
import GDG.whatssue.entity.Schedule;
import GDG.whatssue.repository.ClubRepository;
import GDG.whatssue.repository.ScheduleRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ClubRepository clubRepository;

    public void saveSchedule(Long clubId, AddScheduleRequestDto requestDto) {
        Club club = clubRepository.findById(clubId).orElseThrow(
            () -> new NoSuchElementException());

        Schedule saveSchedule = requestDto.toEntity(club);
        scheduleRepository.save(saveSchedule);
    }

    /***
     clubId 필요 여부
     ***/
    public GetScheduleResponseDto findSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
            () -> new NoSuchElementException());


        GetScheduleResponseDto scheduleDetailDto = GetScheduleResponseDto.builder()
            .scheduleId(schedule.getId())
            .scheduleName(schedule.getScheduleName())
            .scheduleContent(schedule.getScheduleContent())
            .scheduleDate(schedule.getScheduleDate().toString())
            .scheduleTime(schedule.getScheduleTime().toString()).build();

        return scheduleDetailDto;
    }

    @Transactional
    public void updateSchedule(Long scheduleId, ModifyScheduleRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
            () -> new NoSuchElementException());

        schedule.update(
            requestDto.getScheduleName(),
            requestDto.getScheduleContent(),
            requestDto.getScheduleDate(),
            requestDto.getScheduleTime());

        scheduleRepository.save(schedule);
    }

    /***
     clubId 필요 여부
     hard / soft delete 여부
    ***/
    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }
}

