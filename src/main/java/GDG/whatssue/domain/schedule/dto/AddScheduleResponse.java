package GDG.whatssue.domain.schedule.dto;

import lombok.Getter;

@Getter
public class AddScheduleResponse {
    private Long scheduleId;

    public AddScheduleResponse(Long scheduleId) {
        this.scheduleId = scheduleId;
    }
}
