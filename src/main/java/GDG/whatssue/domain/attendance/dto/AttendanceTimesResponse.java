package GDG.whatssue.domain.attendance.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AttendanceTimesResponse {

    private Long attendanceTimes;

    private Long absenceTimes;

    private Long officialAbsenceTimes;

    // 기본 생성자 추가 (필요한 경우)
    public AttendanceTimesResponse() {}

    // 모든 필드를 받는 생성자
    public AttendanceTimesResponse(Long attendanceTimes, Long absenceTimes, Long officialAbsenceTimes) {
        this.attendanceTimes = attendanceTimes;
        this.absenceTimes = absenceTimes;
        this.officialAbsenceTimes = officialAbsenceTimes;
    }

}
