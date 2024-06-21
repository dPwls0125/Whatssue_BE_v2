package GDG.whatssue.global.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();
    String getCode();
    HttpStatus getHttpStatus();
    String getMessage();
}
