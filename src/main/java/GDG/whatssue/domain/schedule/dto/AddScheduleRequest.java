package GDG.whatssue.domain.schedule.dto;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.schedule.entity.Schedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate scheduleDate;

    @NotBlank
    @JsonFormat(pattern = "HH:mm")
    private LocalTime scheduleTime;


    public Schedule toEntity(Club club, ClubMember register) {
        return Schedule.createSchedule(
            club, register, scheduleName,
            scheduleContent, scheduleDate, scheduleTime, schedulePlace);
    }
}
