package GDG.whatssue.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AttendanceNumResponseDto{
    private Long clubId;
    private Long scheduleId;
    private int AttendanceNum;
}
