package GDG.whatssue.domain.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class ModifyScheduleRequest {

    @NotBlank
    private String scheduleName;

    @NotBlank
    private String scheduleContent;

    @NotBlank
    private String schedulePlace;

    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate scheduleDate;

    @NotBlank
    @JsonFormat(pattern = "HH:mm")
    private LocalTime scheduleTime;
}
