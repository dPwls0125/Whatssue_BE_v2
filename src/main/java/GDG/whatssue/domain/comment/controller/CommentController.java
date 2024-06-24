package GDG.whatssue.domain.comment.controller;

import GDG.whatssue.domain.comment.dto.ChildCommentAddDto;
import GDG.whatssue.domain.comment.dto.CommentAddDto;
import GDG.whatssue.domain.comment.service.CommentService;
import GDG.whatssue.domain.comment.service.CommentServiceImpl;
import GDG.whatssue.global.common.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


}
