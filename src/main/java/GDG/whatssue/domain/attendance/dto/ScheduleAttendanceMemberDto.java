package GDG.whatssue.domain.attendance.dto;

import GDG.whatssue.domain.attendance.entity.AttendanceType;
import GDG.whatssue.domain.attendance.entity.ScheduleAttendanceResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ScheduleAttendanceMemberDto {

    private Long clubId;

    private Long scheduleId;

    private Long clubMemberId;

    private String clubMemberName;

    private AttendanceType attendanceType;


    public static ScheduleAttendanceMemberDto of(ScheduleAttendanceResult entity){
        return ScheduleAttendanceMemberDto.builder()
                .scheduleId(entity.getSchedule().getId())
                .clubId(entity.getSchedule().getClub().getId())
                .clubMemberName(entity.getClubMember().getMemberName())
                .attendanceType(entity.getAttendanceType())
                .clubMemberId(entity.getClubMember().getId())
                .build();
    }

    public static List<ScheduleAttendanceMemberDto> of(List<ScheduleAttendanceResult> list){
            return list.stream()
                    .map(ScheduleAttendanceMemberDto::of)
                    .toList();
    }

}
