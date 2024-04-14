package GDG.whatssue.domain.schedule.dto;

import GDG.whatssue.domain.schedule.entity.AttendanceStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetScheduleDetailResponse {

    private Long scheduleId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime scheduleDateTime;
    private String scheduleName;
    private String scheduleContent;
    private String schedulePlace;
    private String registerName;
    private String registerProfileImage;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime registerTime;
    private AttendanceStatus attendanceStatus;

    @Builder
    public GetScheduleDetailResponse(
        Long scheduleId, String scheduleName, String scheduleContent,
        LocalDateTime scheduleDateTime, String schedulePlace,
        String registerName, String registerProfileImage, LocalDateTime registerTime, AttendanceStatus attendanceStatus) {
        this.scheduleId = scheduleId;
        this.scheduleName = scheduleName;
        this.scheduleContent = scheduleContent;
        this.scheduleDateTime = scheduleDateTime;
        this.schedulePlace = schedulePlace;
        this.registerName = registerName;
        this.registerProfileImage = registerProfileImage;
        this.registerTime = registerTime;
        this.attendanceStatus = attendanceStatus;
    }
}
