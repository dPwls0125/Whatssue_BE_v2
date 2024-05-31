package GDG.whatssue.domain.clubjoinrequest.controller;

import GDG.whatssue.domain.clubjoinrequest.dto.ClubJoinRequest;
import GDG.whatssue.domain.clubjoinrequest.service.ClubJoinService;
import GDG.whatssue.domain.clubjoinrequest.dto.GetJoinClubResponse;
import GDG.whatssue.global.common.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ClubJoinController", description = "유저의 모임가입과 가입요청 처리에 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
@CrossOrigin
public class ClubJoinController {

    private final ClubJoinService clubJoinService;

    @Operation(summary = "가입한 모임 조회")
    @GetMapping
    public ResponseEntity<Page<GetJoinClubResponse>> getJoinClubList(@LoginUser Long userId, Pageable pageable) {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(clubJoinService.getJoinClubList(userId, pageable));
    }

    @Operation(summary = "모임가입 코드로 모임 정보 조회")
    @PostMapping("/join")
    public ResponseEntity<String> findClubByPrivateCode(@Valid @RequestBody ClubJoinRequest requestDto) {

        clubJoinService.findClubByPrivateCode(requestDto.getPrivateCode());

        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @Operation(summary = "모임가입 신청")
    @PostMapping("/join/{clubId}")
    public ResponseEntity<String> joinClub(@LoginUser Long userId, @PathVariable(name = "clubId") Long clubId) {

        clubJoinService.joinClub(userId, clubId);

        return new ResponseEntity("ok", HttpStatus.OK);
    }

    @Operation(summary = "모임가입 신청내역 조회")
    @GetMapping("/join")
    public ResponseEntity getJoinRequests(@LoginUser Long userId, Pageable pageable) {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(clubJoinService.getJoinRequests(userId, pageable));
    }
    
    @Operation(summary = "모임가입 신청 내역 상세조회")
    @GetMapping("/join/{joinRequestId}")
    public ResponseEntity getJoinRequestDetail(@LoginUser Long userId, @PathVariable(name = "joinRequestId") Long joinRequestId) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(clubJoinService.getJoinRequestRejectionReason(userId, joinRequestId));
    }
}
