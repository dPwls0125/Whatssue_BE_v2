package GDG.whatssue.domain.attendance.dto;

import GDG.whatssue.domain.attendance.entity.AttendanceType;
import lombok.*;


@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class ScheduleAttendanceMemberDto {

    private Long clubId;

    private Long scheduleId;

    private Long clubMemberId;

    private AttendanceType attendanceType;

}
