package GDG.whatssue.domain.clubjoinrequest.controller;

import GDG.whatssue.domain.clubjoinrequest.dto.ClubJoinRequestGetDto;
import GDG.whatssue.domain.clubjoinrequest.service.ClubJoinManageService;
import GDG.whatssue.global.common.annotation.ClubManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ClubJoinManageController", description = "모임 가입요청 처리에 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}/join")
@CrossOrigin
public class ClubJoinManageController {

    private final ClubJoinManageService memberJoinService;

    @ClubManager
    @Operation(summary="멤버 가입요청 수락")
    @PostMapping("/{clubJoinRequestId}/accept")
    public ResponseEntity acceptClubJoinRequest(@PathVariable Long clubJoinRequestId){
        memberJoinService.acceptResponse(clubJoinRequestId);

        return ResponseEntity.status(200).body("가입 신청 수락 완료");
    }

    @ClubManager
    @Operation(summary="멤버 가입요청 거절")
    @PostMapping("/{clubJoinRequestId}/deny")
    public ResponseEntity denyClubJoinRequest(@PathVariable Long clubJoinRequestId){
        memberJoinService.denyResponse(clubJoinRequestId);

        return ResponseEntity.status(200).body("가입 신청 거절 완료");
    }

    @ClubManager
    @Operation(summary = "멤버 가입요청 목록 조회")
    @GetMapping
    public ResponseEntity<List<ClubJoinRequestGetDto>> getClubJoinRequest(@PathVariable Long clubId){
        List<ClubJoinRequestGetDto>ClubJoinRequests = memberJoinService.getClubJoinRequests(clubId);

        return ResponseEntity.ok(ClubJoinRequests);
    }
}
