package GDG.whatssue.service;

import GDG.whatssue.dto.schedule.AddScheduleRequestDto;
import GDG.whatssue.dto.schedule.GetScheduleResponseDto;
import GDG.whatssue.entity.Club;
import GDG.whatssue.entity.Schedule;
import GDG.whatssue.repository.ClubRepository;
import GDG.whatssue.repository.ScheduleRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ClubRepository clubRepository;

    public void saveSchedule(Long clubId, AddScheduleRequestDto scheduleRequestDto) {
        Club club = clubRepository.findById(clubId).get();
        Schedule saveSchedule = scheduleRequestDto.toEntity(club);
        scheduleRepository.save(saveSchedule);
    }

    /***
     clubId 필요 여부
     ***/
    public GetScheduleResponseDto findSchedule(Long clubId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElse(null);

        if (schedule != null) {
            GetScheduleResponseDto scheduleDetailDto = GetScheduleResponseDto.builder()
                .scheduleId(schedule.getId())
                .scheduleName(schedule.getScheduleName())
                .scheduleContent(schedule.getScheduleContent())
                .scheduleDate(schedule.getScheduleDate().toString())
                .scheduleTime(schedule.getScheduleTime().toString()).build();

            return scheduleDetailDto;
        }

        return null;
    }

    /***
     존재하지 않는 일정 처리
     clubId 필요 여부
     hard / soft delete 여부
    ***/
    public void deleteSchedule(Long clubId, Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }
}

