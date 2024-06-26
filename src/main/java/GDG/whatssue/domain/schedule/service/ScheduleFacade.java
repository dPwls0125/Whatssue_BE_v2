package GDG.whatssue.domain.schedule.service;

import GDG.whatssue.domain.schedule.entity.AttendanceStatus;
import GDG.whatssue.domain.schedule.entity.Schedule;
import GDG.whatssue.domain.schedule.exception.ScheduleErrorCode;
import GDG.whatssue.domain.schedule.repository.ScheduleRepository;
import GDG.whatssue.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleFacade {

    private final ScheduleRepository scheduleRepository;

    public List<Schedule> getSchedule(Long clubId){
        List<Schedule>scheduleList =  scheduleRepository.findByClub_Id(clubId)
                .orElseGet(() -> new ArrayList<>());
        return scheduleList;
    }

    public Schedule getSchedule(Long clubId, Long scheduleId){
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(
                        () -> new CommonException(ScheduleErrorCode.EX4100));
    }

    public List<Schedule> getSchedule(Long clubId, AttendanceStatus attendanceStatus){
        return scheduleRepository.findByClub_IdAndAttendanceStatus(clubId, attendanceStatus)
                .orElseGet(() -> new ArrayList<>());
    }
}
