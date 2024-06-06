package GDG.whatssue.domain.post.controller;

import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.entity.Role;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.post.dto.AddPostRequest;
import GDG.whatssue.domain.post.dto.GetPostResponse;
import GDG.whatssue.domain.post.dto.UpdatePostRequest;
import GDG.whatssue.domain.post.entity.PostCategory;
import GDG.whatssue.domain.post.service.PostService;
import GDG.whatssue.global.common.annotation.ClubManager;
import GDG.whatssue.global.common.annotation.LoginMember;
import GDG.whatssue.global.common.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "PostController", description = "모임의 게시글에 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}/posts")
public class PostController {

    private final PostService postService;
    private final ClubMemberRepository clubMemberRepository;

    @Operation(summary="게시글 작성")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity writePost(
        @PathVariable(name = "clubId") Long clubId,
        @LoginUser Long userId,
        @RequestPart("request") AddPostRequest request,
        @RequestPart(value = "postImages", required = false) List<MultipartFile> postImages)
        throws IOException {

        postService.addPost(clubId, userId, request, postImages);
        return ResponseEntity.status(200).body("게시글 작성 완료");
    }

    @GetMapping("/{postId}")
    @Operation(summary="게시글 단일 조회")
    public ResponseEntity getPost(
        @PathVariable(name = "clubId") Long clubId, @PathVariable(name = "postId") Long postId) {
        GetPostResponse responseDto = postService.getPost(postId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/{postId}/delete")
    @Operation(summary="게시글 삭제")
    public ResponseEntity deletePost(
        @PathVariable(name = "clubId") Long clubId,
        @PathVariable(name = "postId") Long postId,
        @LoginMember long memberId
    )throws IOException {
        GetPostResponse responseDto = postService.getPost(postId);
        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId,memberId).get();

        postService.deletePost(postId,memberId);
        return ResponseEntity.status(200).body("게시글 삭제 완료");
    }

    @PatchMapping(value = "/{postId}/modify", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary="게시글 수정")
    public ResponseEntity updatePost(
            @PathVariable(name = "clubId") Long clubId,
            @PathVariable(name = "postId") Long postId,
            @LoginMember Long memberId,
            @RequestPart("request") UpdatePostRequest request,
            @RequestPart(value = "postImages", required = false) List<MultipartFile> postImages) throws IOException {

        postService.updatePost(clubId, memberId, postId, request, postImages);
        return ResponseEntity.status(HttpStatus.OK).body("게시글 수정 완료");
    }
    @Operation(summary = "Search posts", description = "Search posts by keyword, date range, and sorting.")
    @GetMapping("/search")
    public ResponseEntity<Page<GetPostResponse>> searchPosts(
            @PathVariable Long clubId,
            @Parameter(description = "Search keyword", in = ParameterIn.QUERY, required = false)
            @RequestParam(required = false) String keyword,
            @Parameter(description = "Start date for the search range", in = ParameterIn.QUERY, required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date for the search range", in = ParameterIn.QUERY, required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Sorting criteria", in = ParameterIn.QUERY, required = true)
            @RequestParam String sortBy,
            @Parameter(hidden = true) Pageable pageable) {
        Page<GetPostResponse> posts = postService.getPostList(clubId, keyword, startDate, endDate, sortBy, pageable);
        return ResponseEntity.ok(posts);
    }
}
