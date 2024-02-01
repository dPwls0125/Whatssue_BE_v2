package GDG.whatssue.service;

import GDG.whatssue.dto.schedule.AddScheduleRequestDto;
import GDG.whatssue.dto.schedule.GetScheduleResponseDto;
import GDG.whatssue.dto.schedule.ModifyScheduleRequestDto;
import GDG.whatssue.entity.Club;
import GDG.whatssue.entity.Schedule;
import GDG.whatssue.repository.ClubRepository;
import GDG.whatssue.repository.ScheduleRepository;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
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

    @Transactional
    public void updateSchedule(Long scheduleId, ModifyScheduleRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
            () -> new NoSuchElementException());

        schedule.update(
            requestDto.getScheduleName(),
            requestDto.getScheduleContent(),
            requestDto.getScheduleDateTime());

        scheduleRepository.save(schedule);
    }

    /***
     clubId 필요 여부
     hard / soft delete 여부
    ***/
    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
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
            .scheduleDateTime(schedule.getScheduleDateTime().toString()).build();

        return scheduleDetailDto;
    }

    /**
     *클럽이 없는 경우도 생각해야하나?
     * 이미 앞 인증권한 절차에서 클럽과 userId 대조했으니 일단은 보류
     */
    public List<GetScheduleResponseDto> findScheduleAllByFilter(Long clubId, String date, String month) {
        Club findClub = clubRepository.findById(clubId).get();

        List<Schedule> scheduleList = findClub.getScheduleList();
        List<GetScheduleResponseDto> responseDtoList;

        if(date==null && month==null ) { //전체조회
            responseDtoList = ScheduleListToResponseDtoList(scheduleList, null, null);
        } else if (date != null) { //일자별 조회
            responseDtoList = ScheduleListToResponseDtoList(scheduleList, "yyyy-MM-dd", date);
        } else { //월별 조회
            responseDtoList = ScheduleListToResponseDtoList(scheduleList, "yyyy-MM", month);
        }

        return responseDtoList;
    }
    public List<GetScheduleResponseDto> ScheduleListToResponseDtoList(List<Schedule> scheduleList, String pattern, String filter) {

        if (pattern==null || filter==null) { //전체 조회
            return scheduleList.stream()
                .map(s -> GetScheduleResponseDto.builder()
                    .scheduleId(s.getId())
                    .scheduleName(s.getScheduleName())
                    .scheduleContent(s.getScheduleContent())
                    .scheduleDateTime(s.getScheduleDateTime().toString()).build())
                .collect(Collectors.toList());
        } else { //필터링 조회
        return scheduleList.stream()
            .filter(s-> s.getScheduleDateTime().format(DateTimeFormatter.ofPattern(pattern)).equals(filter))
            .map(s -> GetScheduleResponseDto.builder()
                .scheduleId(s.getId())
                .scheduleName(s.getScheduleName())
                .scheduleContent(s.getScheduleContent())
                .scheduleDateTime(s.getScheduleDateTime().toString()).build())
            .collect(Collectors.toList());
        }
    }
}

