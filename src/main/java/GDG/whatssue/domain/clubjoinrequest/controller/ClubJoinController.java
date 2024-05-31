package GDG.whatssue.domain.clubjoinrequest.controller;

import GDG.whatssue.domain.clubjoinrequest.dto.ClubJoinRequest;
import GDG.whatssue.domain.clubjoinrequest.dto.GetClubInfoByPrivateCodeResponse;
import GDG.whatssue.domain.clubjoinrequest.dto.GetJoinRequestsResponse;
import GDG.whatssue.domain.clubjoinrequest.dto.GetRejectionReasonResponse;
import GDG.whatssue.domain.clubjoinrequest.service.ClubJoinService;
import GDG.whatssue.domain.clubjoinrequest.dto.GetJoinClubResponse;
import GDG.whatssue.global.common.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ClubJoinController", description = "유저의 모임가입과 가입요청 처리에 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/join")
@CrossOrigin
@Validated
public class ClubJoinController {

    private final ClubJoinService clubJoinService;

    @Operation(summary = "모임가입 코드로 모임 정보 조회")
    @GetMapping("/club")
    public ResponseEntity<GetClubInfoByPrivateCodeResponse> findClubByPrivateCode(
        @RequestParam("privateCode")
            @NotBlank(message = "가입코드는 필수 입력값입니다.")
                @Size(min = 6, max = 6, message = "클럽 가입코드는 6자리입니다") String privateCode) {

        return ResponseEntity.status(HttpStatus.OK).body(clubJoinService.findClubByPrivateCode(privateCode));
    }

    @Operation(summary = "모임가입 신청")
    @PostMapping("/club/{clubId}")
    public ResponseEntity<String> joinClub(@LoginUser Long userId, @PathVariable(name = "clubId") Long clubId) {

        clubJoinService.joinClub(userId, clubId);

        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @Operation(summary = "모임가입 신청내역 조회")
    @GetMapping("/requests")
    public ResponseEntity<Page<GetJoinRequestsResponse>> getJoinRequests(@LoginUser Long userId, Pageable pageable) {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(clubJoinService.getJoinRequests(userId, pageable));
    }
    
    @Operation(summary = "모임가입 신청 거절 사유 조회")
    @GetMapping("/requests/{joinRequestId}")
    public ResponseEntity<GetRejectionReasonResponse> getJoinRequestDetail(@LoginUser Long userId, @PathVariable(name = "joinRequestId") Long joinRequestId) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(clubJoinService.getJoinRequestRejectionReason(userId, joinRequestId));
    }
}
