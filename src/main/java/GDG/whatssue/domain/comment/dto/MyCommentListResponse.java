package GDG.whatssue.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class MyCommentListResponse {

    private Long memberId;
    private String clubMemberImage;
    private String memberName;

    private Page<MyCommentDto> myCommentList;


}
