package GDG.whatssue.global.error;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResult {

    private final String code;
    private final String message;
    private final String path;
}
