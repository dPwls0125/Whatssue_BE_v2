package GDG.whatssue.service;

import GDG.whatssue.dto.schedule.ScheduleAddRequestDto;
import GDG.whatssue.entity.Club;
import GDG.whatssue.repository.ClubRepository;
import GDG.whatssue.repository.ScheduleRepository;
import ch.qos.logback.core.joran.sanity.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private ScheduleRepository scheduleRepository;
    private final ClubRepository clubRepository;

    public void createSchedule(Long clubId, ScheduleAddRequestDto scheduleRequestDto) {
        Club findClub = clubRepository.findById(clubId).get();
        scheduleRepository.save(scheduleRequestDto.toEntity(findClub));
    }



}

