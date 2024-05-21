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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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

    @Operation
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity writePost(
        @PathVariable(name = "clubId") Long clubId,
        @LoginMember Long memberId,
        @RequestPart("request") AddPostRequest request,
        @RequestPart(value = "postImages", required = false) List<MultipartFile> postImages)
        throws IOException {
        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId,memberId).get();

        if(request.getPostCategory()==PostCategory.NOTICE){ //공지 조건
            if(clubMember.getRole()== Role.MANAGER){//관리자 조건
                postService.addPost(clubId, memberId, request, postImages);
                return ResponseEntity.status(200).body("공지글 작성 완료");
            }
            else{
                //MANAGER가 아닐 경우 공지 작성 불가 에러 반환 TODO
                return null;
            }
        }
        postService.addPost(clubId, memberId, request, postImages);
        return ResponseEntity.status(200).body("게시글 작성 완료");
    }

    @GetMapping("/{postId}")
    public ResponseEntity getPost(
        @PathVariable(name = "clubId") Long clubId, @PathVariable(name = "postId") Long postId) {
        GetPostResponse responseDto = postService.getPost(postId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/{postId}/delete")
    public ResponseEntity deletePost( //공지 게시글 삭제 메소드 별도 or 통합 TODO
        @PathVariable(name = "clubId") Long clubId,
        @PathVariable(name = "postId") Long postId,
        @LoginMember long memberId
    )throws IOException {
        GetPostResponse responseDto = postService.getPost(postId);
        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId,memberId).get();

        if(responseDto.getPostCategory()==PostCategory.NOTICE){ //공지 조건
            if(clubMember.getRole()== Role.MANAGER){//관리자 조건
                postService.deletePost(postId);
                return ResponseEntity.status(200).body("공지글 삭제 완료");
            }
            else{
                //MANAGER가 아닐 경우 공지 삭제 불가 에러 반환 TODO
                return null;
            }
        }
        if(responseDto.getWriterName() != clubMember.getMemberName()){
            //일반게시글 작성자와 로그인 유저 불일치 시 삭제 불가 에러 반환 TODO
            return null;
        }
        else{
            postService.deletePost(postId);
            return ResponseEntity.status(200).body("게시글 삭제 완료");
        }
    }

    @PatchMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity updatePost (
            @PathVariable(name = "clubId") Long clubId,
            @PathVariable(name = "postId") Long postId,
            @LoginMember Long memberId,
            @RequestPart("request") UpdatePostRequest request,
            @RequestPart(value = "postImages", required = false) List<MultipartFile> postImages
    ) throws IOException {
        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId,memberId).get();

        if(request.getPostCategory()==PostCategory.NOTICE){ //공지 조건
            if(clubMember.getRole()== Role.MANAGER){//관리자 조건
                postService.updatePost(clubId, memberId, postId, request, postImages);
                return ResponseEntity.status(200).body("공지글 수정 완료");
            }
            else{
                //MANAGER가 아닐 경우 공지 수정 불가 에러 반환 TODO
                return null;
            }
        }
        if(request.getWriterName() != clubMember.getMemberName()){
            //일반게시글 작성자와 로그인 유저 불일치 시 삭제 불가 에러 반환 TODO
            return null;
        }
        else{
            postService.updatePost(clubId, memberId, postId, request, postImages);
            return ResponseEntity.status(200).body("게시글 수정 완료");
        }
    }
}
