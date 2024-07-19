package GDG.whatssue.domain.attendance.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AttendanceTimesResponse {

    private Long attendanceTimes;

    private Long absenceTimes;

    private Long officialAbsenceTimes;

}
