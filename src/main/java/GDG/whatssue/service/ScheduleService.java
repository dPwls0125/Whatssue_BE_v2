package GDG.whatssue.service;

import GDG.whatssue.dto.schedule.AddScheduleRequestDto;
import GDG.whatssue.dto.schedule.GetScheduleResponseDto;
import GDG.whatssue.entity.Club;
import GDG.whatssue.entity.Schedule;
import GDG.whatssue.repository.ClubRepository;
import GDG.whatssue.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ClubRepository clubRepository;

    public void createSchedule(Long clubId, AddScheduleRequestDto scheduleRequestDto) {
        Club findClub = clubRepository.findById(clubId).get();
        scheduleRepository.save(scheduleRequestDto.toEntity(findClub));
    }

    public GetScheduleResponseDto findSchedule(Long scheduleId) {
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
}

