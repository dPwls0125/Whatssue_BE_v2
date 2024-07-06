package GDG.whatssue.domain.member.controller;

import GDG.whatssue.domain.member.dto.ClubMemberDto;
import GDG.whatssue.domain.member.dto.ClubMemberInfoDto;
import GDG.whatssue.domain.member.dto.CreateMemberProfileRequest;
import GDG.whatssue.domain.member.dto.MemberAuthInfoResponse;
import GDG.whatssue.domain.member.dto.MemberProfileDto;
import GDG.whatssue.domain.member.service.ClubMemberManagingService;
import GDG.whatssue.domain.member.service.ClubMemberService;
import GDG.whatssue.global.common.annotation.ClubManager;
import GDG.whatssue.global.common.annotation.LoginUser;
import GDG.whatssue.global.common.annotation.SkipFirstVisitCheck;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}/member")
public class ClubMemberController {
    private final ClubMemberService clubMemberService;
    private final ClubMemberManagingService clubMemberManagingService;

    @ClubManager
    @DeleteMapping("/{memberId}/manager")
    @Operation(summary = "멤버 추방", description = "멤버를 클럽에서 추방합니다.(매니저만 이용 가능한 기능)")
    public ResponseEntity deleteMember(@PathVariable Long clubId, @PathVariable Long memberId) {
        clubMemberManagingService.deleteClubMember(memberId);
        return new ResponseEntity("ok", HttpStatus.OK);
    }

    @ClubManager
    @PatchMapping ("/role")
    @Operation(summary = "멤버 권한 수정", description = "멤버의 권한을 수정합니다. role 은 string 형태로 'member' or 'manager'와 같이 입력해야 합니다.(대소문자 구분 x)")
    public ResponseEntity modifyMemberRole(@PathVariable Long clubId, @PathVariable Long memberId, @RequestParam("role") String role) {
        clubMemberManagingService.modifyClubMemberRole(memberId, role);
        return new ResponseEntity("ok", HttpStatus.OK);
    }

    @PostMapping(value = "/profile/modify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "멤버 프로필 수정")
    public ResponseEntity<Void> modifyProfile(
            @LoginUser Long userId,
            @PathVariable Long clubId,
            @Valid @ModelAttribute CreateMemberProfileRequest request) throws IOException {

        clubMemberService.modifyClubMember(clubId,userId, request);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @SkipFirstVisitCheck
    @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary ="멤버 프로필 등록")
    public ResponseEntity<Void> setProfile(
            @LoginUser Long userId,
            @PathVariable Long clubId,
            @Valid @ModelAttribute CreateMemberProfileRequest request) throws IOException {

        clubMemberService.setMemberProfile(clubId, userId, request);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @GetMapping("/member/profile")
    @Operation(summary = "프로필 조회 ( 멤버 + 유저 )")
    public ResponseEntity getProfile(@PathVariable Long clubId, @LoginUser Long userId){
        MemberProfileDto response = clubMemberService.getMemberProfile(clubId,userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/my/auth")
    public ResponseEntity<MemberAuthInfoResponse> getMemberAuthInfo(@PathVariable Long clubId, @LoginUser Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(clubMemberService.getMemberAuthInfo(clubId, userId));
    }
}