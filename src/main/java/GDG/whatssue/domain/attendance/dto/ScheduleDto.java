package GDG.whatssue.domain.attendance.dto;

import GDG.whatssue.domain.schedule.entity.AttendanceStatus;
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

}
