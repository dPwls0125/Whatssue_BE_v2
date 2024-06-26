package GDG.whatssue.domain.officialabsence.controller;

import GDG.whatssue.domain.officialabsence.dto.OfficialAbsenceAddRequestDto;
import GDG.whatssue.domain.officialabsence.dto.OfficialAbsenceGetRequestDto;
import GDG.whatssue.domain.officialabsence.service.OfficialAbsenceService;
import GDG.whatssue.global.common.annotation.ClubManager;
import GDG.whatssue.global.common.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{clubId}/official-absence")
public class OfficialAbsenceController {
    private final OfficialAbsenceService officialAbsenceService;

    @PostMapping(value="/request/{scheduleId}")
    @Operation(summary="공결 신청")
    public ResponseEntity addOfficialAbsenceRequest(
            @LoginUser Long userId,
            @PathVariable Long clubId,
            @PathVariable Long scheduleId,
            @RequestBody String officialAbsenceContent) {
        officialAbsenceService.createOfficialAbsenceRequest(userId, clubId, scheduleId, officialAbsenceContent);
        return ResponseEntity.status(200).body("공결 신청 완료");
    }
    @GetMapping(value="/my-list")
    @Operation(summary="내 공결 신청 조회")
    public ResponseEntity<Page<OfficialAbsenceGetRequestDto>> getMyOfficialAbsenceRequestList(
            @LoginUser Long userId,
            @PathVariable Long clubId,
            Pageable pageable){
        Page<OfficialAbsenceGetRequestDto> officialAbsenceRequests = officialAbsenceService.getMyOfficialAbsenceRequests(userId, clubId, pageable);
        return ResponseEntity.ok(officialAbsenceRequests);
    }
    @DeleteMapping(value="/delete/{officialAbsenceId}")
    @Operation(summary="내 공결 신청 취소")
    public ResponseEntity deleteOfficialAbsence(
            @LoginUser Long userId,
            @PathVariable Long clubId,
            @PathVariable Long officialAbsenceId){
        officialAbsenceService.deleteOfficialAbsences(userId, clubId, officialAbsenceId);
        return ResponseEntity.status(200).body("공결 신청 취소 완료");
    }
    @GetMapping(value="/list")
    @Operation(summary="전체 공결 신청 조회(MANAGER)")
    @ClubManager
    public ResponseEntity<Page<OfficialAbsenceGetRequestDto>> getAllOfficialAbsenceRequestList(
            @PathVariable Long clubId,
            Pageable pageable){
        Page<OfficialAbsenceGetRequestDto> officialAbsenceRequests = officialAbsenceService.getAllOfficialAbsenceRequests(clubId, pageable);
        return ResponseEntity.ok(officialAbsenceRequests);
    }
    @GetMapping(value="/request-list")
    @Operation(summary="대기중인 공결 신청 조회(MANAGER)")
    @ClubManager
    public ResponseEntity<Page<OfficialAbsenceGetRequestDto>> getWaitingOfficialAbsenceRequestList(
            @PathVariable Long clubId,
            Pageable pageable){
        Page<OfficialAbsenceGetRequestDto> officialAbsenceRequests = officialAbsenceService.getWaitingOfficialAbsenceRequests(clubId, pageable);
        return ResponseEntity.ok(officialAbsenceRequests);
    }
    @GetMapping(value="/done-list")
    @Operation(summary="과거 공결 신청 내역 조회(MANAGER)")
    @ClubManager
    public ResponseEntity<Page<OfficialAbsenceGetRequestDto>> getDoneOfficialAbsenceRequestList(
            @PathVariable Long clubId,
            Pageable pageable){
        Page<OfficialAbsenceGetRequestDto> officialAbsenceRequests = officialAbsenceService.getDoneOfficialAbsenceRequests(clubId, pageable);
        return ResponseEntity.ok(officialAbsenceRequests);
    }
    @GetMapping(value="/detail/{officialAbsenceId}")
    @Operation(summary="공결 신청 상세 조회(MANAGER)")
    @ClubManager
    public ResponseEntity<OfficialAbsenceGetRequestDto> getOfficialAbsenceRequestDetail(
            @PathVariable Long clubId,
            @PathVariable Long officialAbsenceId){
        OfficialAbsenceGetRequestDto officialAbsenceRequest = officialAbsenceService.getOfficialAbsenceRequestDetail(clubId, officialAbsenceId);
        return ResponseEntity.ok(officialAbsenceRequest);
    }

    @PostMapping(value="/accept/{officialAbsenceId}")
    @Operation(summary="공결 신청 수락 (MANAGER)")
    @ClubManager
    public ResponseEntity acceptOfficialAbsence(
            @PathVariable Long clubId,
            @PathVariable Long officialAbsenceId){
        officialAbsenceService.acceptResponse(clubId, officialAbsenceId);
        return ResponseEntity.status(200).body("공결 신청 수락 완료");
    }

    @PostMapping(value="/reject/{officialAbsenceId}")
    @Operation(summary="공결 신청 거절 (MANAGER)")
    @ClubManager
    public ResponseEntity denyOfficialAbsence(
            @PathVariable Long clubId,
            @PathVariable Long officialAbsenceId){
        officialAbsenceService.denyResponse(clubId, officialAbsenceId);
        return ResponseEntity.status(200).body("공결 신청 거절 완료");
    }
}