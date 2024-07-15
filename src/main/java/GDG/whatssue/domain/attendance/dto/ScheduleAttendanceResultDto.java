package GDG.whatssue.domain.attendance.dto;

import GDG.whatssue.domain.attendance.entity.AttendanceType;
import GDG.whatssue.domain.attendance.entity.ScheduleAttendanceResult;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ScheduleAttendanceResultDto {

    private Long id;
    private Long scheduleId;
    private String scheduleTitle;
    private AttendanceType attendanceType;
    private String clubMemberName;
    private Long clubMemberId;
    private LocalDate scheduleDate;

    public static ScheduleAttendanceResultDto of(ScheduleAttendanceResult entity){
        return ScheduleAttendanceResultDto.builder()
                .id(entity.getId())
                .scheduleId(entity.getSchedule().getId())
                .scheduleTitle(entity.getSchedule().getScheduleName())
                .attendanceType(entity.getAttendanceType())
                .clubMemberName(entity.getClubMember().getMemberName())
                .clubMemberId(entity.getClubMember().getId())
                .scheduleDate(entity.getSchedule().getScheduleDate().toLocalDate())
                .build();
    }

}
