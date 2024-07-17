package GDG.whatssue.domain.attendance.dto;

import GDG.whatssue.domain.attendance.entity.ScheduleAttendanceResult;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Setter
@Builder
@Getter
public class MyAttendanceResultResponse {

    Long memberId;

    String memberName;

    PageImpl<ScheduleAttendanceResultDto> attendanceList;
}
