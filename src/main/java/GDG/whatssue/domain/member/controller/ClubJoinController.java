package GDG.whatssue.domain.member.controller;

import GDG.whatssue.domain.member.dto.ClubJoinRequestDto;
import GDG.whatssue.domain.member.entity.ClubJoinRequestGetDto;
import GDG.whatssue.domain.member.service.ClubMemberService;
import GDG.whatssue.domain.member.service.MemberJoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 모임 가입 신청 : [POST] - /api/clubs/{clubId}/join TODO
 * 신청 내역 조회 : [GET] - /api/clubs/{clubId}/join TODO
 * 신청 취소 : [DELETE] - /api/clubs/{clubId}/join-cancle TODO
 * 가입 요청 조회 : [GET] - /api/clubs/{clubId}/join-requests TODO
 * 가입 요청 수락 : [POST] - /api/clubs/{clubId}/join-requests/{clubJoinRequestId}/accept TODO
 * 가입 요청 거절 : [POST] - /api/club/{clubId}/join-requests/{clubJoinRequestId}/deny TODO
 **/

@Tag(name = "ClubJoinController", description = "유저의 모임가입과 가입요청 처리에 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}")
public class ClubJoinController {

    private final ClubMemberService clubMemberService;
    private final MemberJoinService memberJoinService;

    @Operation(summary = "모임 가입신청")
    @PostMapping("/join")
    public ResponseEntity joinClub(@Valid @RequestBody ClubJoinRequestDto requestDto) {
        //현재 로그인 id parameter로 받아오기 & 예외처리 TODO
        Long userId = 1L;

        //validation 예외처리 TODO

        clubMemberService.addClubJoinRequest(userId, requestDto);

        return new ResponseEntity("ok", HttpStatus.OK);
    }

    @Operation(summary="멤버 가입요청 수락")
    @PostMapping("/join-requests/{clubJoinRequestId}/accept")
    public ResponseEntity acceptClubJoinRequest(@PathVariable Long clubJoinRequestId){
        memberJoinService.acceptResponse(clubJoinRequestId);

        return ResponseEntity.status(200).body("가입 신청 수락 완료");
    }

    @Operation(summary="멤버 가입요청 거절")
    @PostMapping("/join-requests/{clubJoinRequestId}/deny")
    public ResponseEntity denyClubJoinRequest(@PathVariable Long clubJoinRequestId){
        memberJoinService.denyResponse(clubJoinRequestId);

        return ResponseEntity.status(200).body("가입 신청 거절 완료");
    }

    @Operation(summary = "멤버 가입요청 목록 조회")
    @GetMapping("/join-requests")
    public ResponseEntity<List<ClubJoinRequestGetDto>> getClubJoinRequest(@PathVariable Long clubId){
        List<ClubJoinRequestGetDto>ClubJoinRequests = memberJoinService.getClubJoinRequests(clubId);

        return ResponseEntity.ok(ClubJoinRequests);
    }
}
