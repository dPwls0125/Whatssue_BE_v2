package GDG.whatssue.domain.comment.controller;

import GDG.whatssue.domain.comment.dto.ChildCommentAddDto;
import GDG.whatssue.domain.comment.dto.CommentAddDto;
import GDG.whatssue.domain.comment.dto.CommentUpdateDto;
import GDG.whatssue.domain.comment.service.CommentService;
import GDG.whatssue.domain.comment.service.CommentServiceImpl;
import GDG.whatssue.global.common.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}/comment")
public class CommentController {

    @Qualifier("commentServiceImpl")
    private final CommentService  commentService;

    @Operation(summary = "부모 댓글 생성(대댓글 x)")
    @PostMapping
    public ResponseEntity createComment(@RequestBody CommentAddDto dto, @LoginUser Long userId, @PathVariable Long clubId) {
        commentService.createComment(dto, userId, clubId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "대댓글 생성")
    @PostMapping("/child")
    public ResponseEntity createChildComment(@RequestBody ChildCommentAddDto dto, @LoginUser Long userId, @PathVariable Long clubId) {
        commentService.createChildComment(dto, userId, clubId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary ="댓글 삭제")
    @PostMapping("/{commentId}/delete")
    public ResponseEntity deleteComment(@PathVariable Long commentId, @LoginUser Long userId, @PathVariable Long clubId) {
        commentService.deleteComment(commentId, userId, clubId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "댓글 수정")
    @PostMapping("/update")
    public ResponseEntity updateComment(@RequestBody CommentUpdateDto dto, @LoginUser Long userId, @PathVariable Long clubId) {
        commentService.updateComment(dto, userId, clubId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @Operation(summary = "댓글 리스트 조회")
    @GetMapping("/{postId}")
    public ResponseEntity getCommentList(@PathVariable Long postId, @PathVariable(name = "clubId") Long clubId, @RequestParam int size, @RequestParam int page) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentList(postId, size, page));
    }
}
