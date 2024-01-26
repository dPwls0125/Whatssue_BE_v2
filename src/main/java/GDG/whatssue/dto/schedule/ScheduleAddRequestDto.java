package GDG.whatssue.dto.schedule;

import GDG.whatssue.entity.Club;
import GDG.whatssue.entity.Schedule;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleAddRequestDto {

    private String scheduleName;
    private String scheduleContent;
    private LocalDate scheduleDate;
    private LocalTime scheduleTime;

    public Schedule toEntity(Club club) {
        return Schedule.builder()
            .club(club)
            .scheduleName(scheduleName)
            .scheduleContent(scheduleContent)
            .scheduleDate(scheduleDate)
            .scheduleTime(scheduleTime)
            .isChecked(false).build();
    }
}
