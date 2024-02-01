package GDG.whatssue.dto.schedule;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyScheduleRequestDto {

    private String scheduleName;
    private String scheduleContent;
    private LocalDateTime scheduleDateTime;
}
