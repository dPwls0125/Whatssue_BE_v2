package GDG.whatssue.domain.attendance.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AttendModifyRequest {

    Long scheduleId;

    List<AttendmodifyDto> attendmodifyDtoList;

}
