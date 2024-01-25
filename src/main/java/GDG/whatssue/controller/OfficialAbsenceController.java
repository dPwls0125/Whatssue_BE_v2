package GDG.whatssue.controller;

import GDG.whatssue.dto.OfficialAbsence.OfficialAbsenceAddRequestDto;
import GDG.whatssue.dto.OfficialAbsence.OfficialAbsenceGetRequestDto;
import GDG.whatssue.service.OfficialAbsenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{clubId}/official_absence")
public class OfficialAbsenceController {
    private final OfficialAbsenceService officialAbsenceService;

    @PostMapping(value="/{scheduleId}/request")
    public ResponseEntity addOfficialAbsenceRequest(
            @PathVariable Long scheduleId,
            @RequestParam("officialAbsenceAddRequestDto") OfficialAbsenceAddRequestDto officialAbsenceAddRequestDto) {

        officialAbsenceService.createOfficialAbsenceRequest(scheduleId, officialAbsenceAddRequestDto);
        return ResponseEntity.status(200).body("success");

    }
    @GetMapping(value="/list")
    public ResponseEntity<List<OfficialAbsenceGetRequestDto>> getOfficialAbsenceRequest(){
        List<OfficialAbsenceGetRequestDto> officialAbsenceRequests = officialAbsenceService.getOfficialAbsenceRequests();
        return ResponseEntity.ok(officialAbsenceRequests);
    }
}
