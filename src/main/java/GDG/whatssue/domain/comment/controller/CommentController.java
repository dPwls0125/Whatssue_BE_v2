package GDG.whatssue.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;

@Controller
public class CommentController {

    @Operation(summary = "댓글 작성")
    public void createComment() {
        // 댓글 작성
    }

    @Operation(summary = "댓글 수정")
    public void updateComment() {
        // 댓글 수정
    }

    @Operation(summary = "댓글 삭제")
    public void deleteComment() {
        // 댓글 삭제
    }

    @Operation(summary = "댓글 조회")
    public void getComment() {
        // 댓글 조회
    }

    @Operation(summary = "댓글 목록 조회")
    public void getCommentList() {
        // 댓글 목록 조회
    }
}
