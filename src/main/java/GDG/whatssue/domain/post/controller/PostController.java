package GDG.whatssue.domain.post.controller;

import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.entity.Role;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.post.dto.AddPostRequest;
import GDG.whatssue.domain.post.dto.GetPostResponse;
import GDG.whatssue.domain.post.dto.UpdatePostRequest;
import GDG.whatssue.domain.post.entity.PostCategory;
import GDG.whatssue.domain.post.exception.PostErrorCode;
import GDG.whatssue.domain.post.service.PostService;
import GDG.whatssue.global.common.annotation.ClubManager;
import GDG.whatssue.global.common.annotation.LoginMember;
import GDG.whatssue.global.common.annotation.LoginUser;
import GDG.whatssue.global.error.CommonException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
        @PathVariable(name = "clubId") Long clubId,
        @PathVariable(name = "postId") Long postId,
        @LoginUser Long userId) {
        GetPostResponse responseDto = postService.getPost(clubId, userId, postId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/{postId}/delete")
    @Operation(summary="게시글 삭제")
    public ResponseEntity deletePost(
        @PathVariable(name = "clubId") Long clubId,
        @PathVariable(name = "postId") Long postId,
        @LoginUser Long userId
    )throws IOException {
        GetPostResponse responseDto = postService.getPost(clubId, userId, postId);
        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId,userId).get();

        postService.deletePost(clubId,userId,postId);
        return ResponseEntity.status(200).body("게시글 삭제 완료");
    }

    @PatchMapping(value = "/{postId}/modify", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary="게시글 수정")
    public ResponseEntity updatePost(
            @PathVariable(name = "clubId") Long clubId,
            @PathVariable(name = "postId") Long postId,
            @LoginUser Long userId,
            @RequestPart("request") UpdatePostRequest request,
            @RequestPart(value = "postImages", required = false) List<MultipartFile> postImages) throws IOException {

        postService.updatePost(clubId, userId, postId, request, postImages);
        return ResponseEntity.status(HttpStatus.OK).body("게시글 수정 완료");
    }
    @Operation(summary = "게시물 검색", description = "검색 : 키워드, 기간(형식 :'yyyy-MM-dd', 기본 1900~2199년), 정렬(default : 최신순(or 'Like' 입력), 카테고리(NOTICE or FREE)")
    @GetMapping
    public ResponseEntity<Page<GetPostResponse>> searchPosts(
            @PathVariable(name = "clubId") Long clubId,
            @LoginUser Long userId,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "startDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam(name = "endDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate,
            @RequestParam(name = "sortBy", required = false, defaultValue = "createAt") String sortBy,
            @RequestParam(name = "category") PostCategory category,
            Pageable pageable){
        // 기본값 설정
        LocalDateTime defaultStartDate = LocalDateTime.of(1900, 1, 1, 0, 0);
        LocalDateTime defaultEndDate = LocalDateTime.of(2199, 12, 31, 23, 59);

        // 입력 값 변환
        LocalDateTime startDateTime = (startDate != null) ?
                LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay() : defaultStartDate;
        LocalDateTime endDateTime = (endDate != null) ?
                LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atTime(LocalTime.MAX) : defaultEndDate;

        Page<GetPostResponse> posts = postService.getPostList(clubId, userId, keyword, startDateTime, endDateTime, sortBy, category, pageable);
        return ResponseEntity.ok(posts);
    }

    @PostMapping(value="/{postId}/like")
    @Operation(summary="게시물 좋아요")
    public ResponseEntity addPostLike(
            @PathVariable Long clubId,
            @LoginUser Long userId,
            @PathVariable Long postId){
        postService.createPostLike(clubId,userId, postId);
        return ResponseEntity.status(200).body("게시물 좋아요 성공");

    }
    @DeleteMapping(value="/{postId}/like")
    @Operation(summary="게시물 좋아요 취소")
    public ResponseEntity deletePostLike(
            @PathVariable Long clubId,
            @LoginUser Long userId,
            @PathVariable Long postId){
        postService.deletePostLike(clubId, userId, postId);
        return ResponseEntity.status(200).body("게시물 좋아요 취소 성공");
    }
    @GetMapping(value="/my_posts")
    @Operation(summary="내가 쓴 글", description = "sort : [\"createAt,desc\"] or [\"createAt,asc\"] 올바르지 않은 값 500에러")
    public ResponseEntity getMyPosts(
            @PathVariable Long clubId,
            @LoginUser Long userId,
            @RequestParam PostCategory postCategory,
            Pageable pageable) {
        Page<GetPostResponse> posts = postService.getMyPosts(clubId, userId, postCategory, pageable);
        return ResponseEntity.ok(posts);
    }
    @GetMapping(value="/my_like_posts")
    @Operation(summary="내가 좋아요한 글", description = "sort : [\"createAt,desc\"] or [\"createAt,asc\"] 올바르지 않은 값 500에러")
    public ResponseEntity getMyLikePosts(
            @PathVariable Long clubId,
            @LoginUser Long userId,
            @RequestParam PostCategory postCategory,
            Pageable pageable) {
        Page<GetPostResponse> posts = postService.getLikedPosts(clubId, userId, postCategory, pageable);
        return ResponseEntity.ok(posts);
    }
}
