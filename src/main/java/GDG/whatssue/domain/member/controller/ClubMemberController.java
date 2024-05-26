package GDG.whatssue.domain.member.controller;

import GDG.whatssue.domain.member.dto.ClubMemberDto;
import GDG.whatssue.domain.member.dto.MemberProfileDto;
import GDG.whatssue.domain.member.service.ClubMemberManagingService;
import GDG.whatssue.domain.member.service.ClubMemberService;
import GDG.whatssue.global.common.annotation.ClubManager;
import GDG.whatssue.global.common.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 멤버 권한 수정 : [PATCH] - /api/clubs/{clubId}/members/{memberId}/manager
 * 멤버 추방 : [DELETE] - /api/clubs/{clubId}/members/{memberId}/manager
 * 멤버 상세조회(일반멤버) : [GET] - /api/clubs/{clubId}/members/{memberId}
 * 멤버 상세조회(관리자) : [GET] - /api/clubs/{clubId}/members/{memberId}/manager
 * 멤버 목록 조회 : [GET] - /api/clubs/{clubId}/members
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}")
public class ClubMemberController {
    private final ClubMemberService clubMemberSerivce;
    private final ClubMemberManagingService clubMemberManagingService;

    @ClubManager
    @DeleteMapping("/member/{memberId}/manager")
    @Operation(summary = "멤버 추방", description = "멤버를 클럽에서 추방합니다.(매니저만 이용 가능한 기능)")
    public ResponseEntity deleteMember(@PathVariable Long clubId, @PathVariable Long memberId) {
        clubMemberManagingService.deleteClubMember(memberId);
        return new ResponseEntity("ok", HttpStatus.OK);
    }

    @ClubManager
    @PatchMapping ("/member/{memberId}/manager")
    @Operation(summary = "멤버 권한 수정", description = "멤버의 권한을 수정합니다. role 은 string 형태로 'member' or 'manager'와 같이 입력해야 합니다.(대소문자 구분 x)")
    public ResponseEntity modifyMemberRole(@PathVariable Long clubId, @PathVariable Long memberId, @RequestParam("role") String role) {
        clubMemberManagingService.modifyClubMemberRole(memberId, role);
        return new ResponseEntity("ok", HttpStatus.OK);
    }

    @PatchMapping("/member/{memberId}")
    @Operation(summary = "멤버 정보 수정")
    public ResponseEntity modifyMemberInfo(@PathVariable Long memberId, ClubMemberDto dto) {
        clubMemberSerivce.modifyClubMember(memberId,dto);
        return new ResponseEntity("ok", HttpStatus.OK);
    }

    @GetMapping("/member/{memberId}/")
    @Operation(summary = "프로필 조회 ( 멤버 + 유저 )")
    public ResponseEntity getProfile(@PathVariable Long memberId, @LoginUser Long userId){
        MemberProfileDto dto = clubMemberSerivce.getMemberProfile(memberId,userId);
        return new ResponseEntity(dto, HttpStatus.OK);
    }

}
