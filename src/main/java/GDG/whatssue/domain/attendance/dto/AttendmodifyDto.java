package GDG.whatssue.domain.attendance.dto;

import GDG.whatssue.domain.attendance.entity.AttendanceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendmodifyDto {

    Long memberId;
    AttendanceType attendanceType;
    Boolean isModified;

}
