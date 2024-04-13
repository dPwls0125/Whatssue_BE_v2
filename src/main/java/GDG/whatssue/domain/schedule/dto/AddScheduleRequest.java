package GDG.whatssue.domain.schedule.dto;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.schedule.entity.AttendanceStatus;
import GDG.whatssue.domain.schedule.entity.Schedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AddScheduleRequest {

    @NotBlank
    private String scheduleName;
    @NotNull
    private String scheduleContent;

    @NotNull
    private String schedulePlace;

    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime scheduleDateTime;

    public Schedule toEntity(Club club, ClubMember registerMember) {
        return Schedule.builder()
            .club(club)
            .scheduleName(scheduleName)
            .scheduleContent(scheduleContent)
            .scheduleDateTime(scheduleDateTime)
            .schedulePlace(schedulePlace)
            .clubMember(registerMember)
            .attendanceStatus(AttendanceStatus.BEFORE).build();
    }
}
