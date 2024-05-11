package GDG.whatssue.domain.post.controller;

import GDG.whatssue.domain.post.dto.AddPostRequest;
import GDG.whatssue.domain.post.dto.GetPostResponse;
import GDG.whatssue.domain.post.service.PostService;
import GDG.whatssue.global.common.annotation.ClubManager;
import GDG.whatssue.global.common.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "PostController", description = "모임의 게시글에 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}/posts")
public class PostController {

    private final PostService postService;

    @Operation
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity writePost(
        @PathVariable(name = "clubId") Long clubId,
        @LoginMember Long memberId,
        @RequestPart("request") AddPostRequest request,
        @RequestPart(value = "postImages", required = false) List<MultipartFile> postImages)
        throws IOException {
        
        //게시글, 공지 구분 및 권한에 따른 예외처리 TODO
        postService.addPost(clubId, memberId, request, postImages);
        return null;
    }

    @GetMapping("/{postId}")
    public ResponseEntity getPost(
        @PathVariable(name = "clubId") Long clubId, @PathVariable(name = "postId") Long postId) {
        GetPostResponse responseDto = postService.getPost(postId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
