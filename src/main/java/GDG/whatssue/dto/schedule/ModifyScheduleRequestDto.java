package GDG.whatssue.dto.schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ModifyScheduleRequestDto {

    private String scheduleName;
    private String scheduleContent;
    private LocalDate scheduleDate;
    private LocalTime scheduleTime;
}
