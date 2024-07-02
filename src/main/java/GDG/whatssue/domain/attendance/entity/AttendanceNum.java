package GDG.whatssue.domain.attendance.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@RedisHash(value = "AttendanceNum") // 문자 인증 2분으로 제한
@Builder
@Data
public class AttendanceNum {

    @Id
    @Indexed
    private String id;
    private int certificationNum;
    private String createdAt;

}
