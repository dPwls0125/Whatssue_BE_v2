package GDG.whatssue.domain.schedule.dto;

import GDG.whatssue.domain.schedule.entity.AttendanceStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ScheduleDetailResponse {

    private Long scheduleId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate scheduleDate;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime scheduleTime;

    private String scheduleName;
    private String scheduleContent;
    private String schedulePlace;
    private String registerName;
    private String registerProfileImage;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime registrationDate;
    private AttendanceStatus attendanceStatus;

    @Builder
    public ScheduleDetailResponse(
        Long scheduleId, String scheduleName, String scheduleContent,
        LocalDateTime scheduleDate, String schedulePlace,
        String registerName, String registerProfileImage, LocalDateTime registerTime, AttendanceStatus attendanceStatus) {
        this.scheduleId = scheduleId;
        this.scheduleName = scheduleName;
        this.scheduleContent = scheduleContent;
        this.scheduleDate = scheduleDate.toLocalDate();
        this.scheduleTime = scheduleDate.toLocalTime();
        this.schedulePlace = schedulePlace;
        this.registerName = registerName;
        this.registerProfileImage = registerProfileImage;
        this.registrationDate = registerTime;
        this.attendanceStatus = attendanceStatus;
    }
}
