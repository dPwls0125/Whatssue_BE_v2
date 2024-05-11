package GDG.whatssue.domain.comment.controller;

import GDG.whatssue.domain.comment.dto.CommentBaseDto;
import GDG.whatssue.domain.comment.dto.CommentCreateDto;
import GDG.whatssue.domain.comment.dto.CommentModifyDto;
import GDG.whatssue.domain.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/{clubId}/member/{memberId}/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;
    @Operation(summary = "댓글 작성")
    @PostMapping("/post")
    public ResponseEntity createComment(CommentCreateDto dto, @PathVariable Long memberId) {
        commentService.createComment(memberId,dto);
        return ResponseEntity.status(200).body("ok");
    }

    @Operation(summary = "댓글 수정")
    @PatchMapping("/update")
    public void updateComment(CommentModifyDto dto, @PathVariable Long memberId, @PathVariable Long commentId) {
        commentService.updateComment(memberId,dto);
    }

    @Operation(summary = "댓글 삭제")
    public void deleteComment(Long memberId, Long commentId) {
        commentService.deleteComment(memberId, commentId);
        // 댓글 삭제
    }

    @Operation(summary = "댓글 조회")
    @GetMapping("/get")
    public ResponseEntity getComment(Long commentId) {
        return ResponseEntity.status(200).body(commentService.getComment(commentId));
    }

    @Operation(summary = "댓글 목록 조회")
    @GetMapping("/list")
    public ResponseEntity getCommentList(Long postId) {
        return ResponseEntity.status(200).body(commentService.getCommentList(postId));
    }
}
