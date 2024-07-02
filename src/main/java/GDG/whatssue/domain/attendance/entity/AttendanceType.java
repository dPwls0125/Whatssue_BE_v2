package GDG.whatssue.domain.attendance.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AttendanceType {
    ATTENDANCE("출석"),
    ABSENCE("결석"),
    OFFICIAL_ABSENCE("공결");

    private final String value;

    AttendanceType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
