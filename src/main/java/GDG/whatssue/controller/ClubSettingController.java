package GDG.whatssue.controller;

import GDG.whatssue.dto.ClubSetting.SettingClubDto;
import GDG.whatssue.service.ClubSettingService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/club_setting")
public class ClubSettingController {
    private final ClubSettingService clubSettingService;

    @PostMapping(value="/create")@Operation(summary="모임 생성 api")
    public ResponseEntity addClub(
            @RequestBody SettingClubDto settingClubDto) {

        clubSettingService.createClub(settingClubDto);
        return ResponseEntity.status(200).body("모임 생성 완료");

    }
    @PatchMapping(value="/modify/{clubId}")@Operation(summary="모임 수정 api")
    public ResponseEntity modifyClub(
            @PathVariable Long clubId,
            @RequestBody SettingClubDto settingClubDto){

        clubSettingService.modifyClub(clubId, settingClubDto);
        return ResponseEntity.status(200).body("모임 수정 완료");

    }
    @DeleteMapping(value="/delete/{clubId}")@Operation(summary="모임 삭제 api")
    public ResponseEntity removeClub(
            @PathVariable Long clubId){
        clubSettingService.deleteClub(clubId);
        return ResponseEntity.status(200).body("클럽 삭제 완료");

    }
}