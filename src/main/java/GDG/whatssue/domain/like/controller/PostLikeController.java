package GDG.whatssue.domain.like.controller;

import GDG.whatssue.domain.like.service.PostLikeService;
import GDG.whatssue.domain.officialabsence.dto.OfficialAbsenceAddRequestDto;
import GDG.whatssue.global.common.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{clubId}/post_like")
public class PostLikeController {
    private final PostLikeService postLikeService;
    @PostMapping(value="/{postId}/")
    @Operation(summary="게시물 좋아요")
    public ResponseEntity addPostLike(
            @PathVariable Long clubId,
            @LoginMember Long memberId,
            @PathVariable Long postId){
        postLikeService.createPostLike(clubId, memberId, postId);
        return ResponseEntity.status(200).body("게시물 좋아요 성공");

    }
    @DeleteMapping(value="/{postId}/")
    @Operation(summary="게시물 좋아요 취소")
    public ResponseEntity deletePostLike(
            @PathVariable Long clubId,
            @LoginMember Long memberId,
            @PathVariable Long postId){
        postLikeService.deletePostLike(clubId, memberId, postId);
        return ResponseEntity.status(200).body("게시물 좋아요 취소 성공");

    }
}
