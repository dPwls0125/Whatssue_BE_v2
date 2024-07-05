package GDG.whatssue.domain.clubjoinrequest.controller;

import GDG.whatssue.domain.clubjoinrequest.dto.GetJoinRequestsResponse;
import GDG.whatssue.domain.clubjoinrequest.dto.GetRejectionReasonResponse;
import GDG.whatssue.domain.clubjoinrequest.dto.JoinClubRequest;
import GDG.whatssue.domain.clubjoinrequest.service.ClubJoinService;
import GDG.whatssue.global.common.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ClubJoinController", description = "유저의 모임가입과 가입요청 처리에 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/join")
@CrossOrigin
public class ClubJoinController {

    private final ClubJoinService clubJoinService;

    @Operation(summary = "모임가입 신청")
    @PostMapping
    public ResponseEntity<String> joinClub(@LoginUser Long userId,
        @Valid @RequestBody JoinClubRequest request) {

        clubJoinService.joinClub(userId, request.getClubId());

        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @Operation(summary = "모임가입 신청내역 조회")
    @GetMapping
    public ResponseEntity<Page<GetJoinRequestsResponse>> getJoinRequests(@LoginUser Long userId, Pageable pageable) {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(clubJoinService.getJoinRequests(userId, pageable));
    }
    
    @Operation(summary = "모임가입 신청 거절 사유 조회")
    @GetMapping("/{joinRequestId}")
    public ResponseEntity<GetRejectionReasonResponse> getJoinRequestDetail(@LoginUser Long userId, @PathVariable(name = "joinRequestId") Long joinRequestId) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(clubJoinService.getJoinRequestRejectionReason(userId, joinRequestId));
    }

    @Operation(summary = "모임가입 신청 삭제")
    @DeleteMapping("/{joinRequestId}")
    public ResponseEntity<String> deleteJoinRequest(@LoginUser Long userId, @PathVariable(name = "joinRequestId") Long joinRequestId) {
        clubJoinService.deleteJoinRequest(userId, joinRequestId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body("ok");
    }

    @Operation(summary = "모임가입 신청 취소")
    @PostMapping("/{joinRequestId}/cancel")
    public ResponseEntity<String> cancelJoinRequest(@LoginUser Long userId, @PathVariable(name = "joinRequestId") Long joinRequestId) {
        clubJoinService.cancelJoinRequest(userId, joinRequestId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body("ok");
    }
}
