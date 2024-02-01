package GDG.whatssue.dto.schedule;

import GDG.whatssue.entity.Club;
import GDG.whatssue.entity.Schedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AddScheduleRequestDto {

    private String scheduleName;
    private String scheduleContent;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime scheduleDateTime;

    public Schedule toEntity(Club club) {
        return Schedule.builder()
            .club(club)
            .scheduleName(scheduleName)
            .scheduleContent(scheduleContent)
            .scheduleDateTime(scheduleDateTime)
            .isChecked(false).build();
    }
}
