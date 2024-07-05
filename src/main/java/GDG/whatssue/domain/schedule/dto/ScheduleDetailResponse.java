package GDG.whatssue.domain.schedule.dto;

import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.schedule.entity.AttendanceStatus;
import GDG.whatssue.domain.schedule.entity.Schedule;
import GDG.whatssue.global.util.S3Utils;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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

    public ScheduleDetailResponse(Schedule schedule, ClubMember register) {
        this.scheduleId = schedule.getId();
        this.scheduleName = schedule.getScheduleName();
        this.scheduleContent = schedule.getScheduleContent();
        this.scheduleDate = schedule.getScheduleDate().toLocalDate();
        this.scheduleTime = schedule.getScheduleDate().toLocalTime();
        this.schedulePlace = schedule.getSchedulePlace();
        this.registerName = schedule.getRegister().getMemberName();
        this.registerProfileImage = S3Utils.getFullPath(register.getProfileImage().getStoreFileName());
        this.registrationDate = schedule.getCreateAt();
        this.attendanceStatus = schedule.getAttendanceStatus();
    }
}
