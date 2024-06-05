package GDG.whatssue.domain.attendance.dto;

import GDG.whatssue.domain.schedule.entity.AttendanceStatus;
import GDG.whatssue.domain.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ScheduleDto {

    Long scheduleId;

    Long clubId;

    String scheduleName;

    String scheduleContent;

    LocalDateTime scheduleDateTime;

    AttendanceStatus attendanceStatus;

    public static ScheduleDto of(Schedule schedule){
        return ScheduleDto.builder()
                .scheduleId(schedule.getId())
                .clubId(schedule.getClub().getId())
                .scheduleName(schedule.getScheduleName())
                .scheduleContent(schedule.getScheduleContent())
                .scheduleDateTime(LocalDateTime.of(schedule.getScheduleDate(), schedule.getScheduleTime()))
                .attendanceStatus(schedule.getAttendanceStatus())
                .build();

    }

}
