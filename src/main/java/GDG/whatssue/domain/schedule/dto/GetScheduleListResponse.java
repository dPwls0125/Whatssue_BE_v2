package GDG.whatssue.domain.schedule.dto;

import GDG.whatssue.domain.schedule.entity.AttendanceStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetScheduleListResponse {

    private Long scheduleId;
    private String scheduleName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime scheduleDateTime;
    private AttendanceStatus attendanceStatus;

    @Builder
    public GetScheduleListResponse(
        Long scheduleId, String scheduleName,
        LocalDateTime scheduleDateTime, AttendanceStatus attendanceStatus) {
        this.scheduleId = scheduleId;
        this.scheduleName = scheduleName;
        this.scheduleDateTime = scheduleDateTime;
        this.attendanceStatus = getAttendanceStatus();
    }
}
