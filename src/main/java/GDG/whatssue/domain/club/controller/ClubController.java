package GDG.whatssue.domain.club.controller;

import GDG.whatssue.domain.club.dto.ClubCreateRequest;
import GDG.whatssue.domain.club.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 모임 생성 : [POST] - /api/clubs
 * 모임 삭제 : [DELETE] - /api/clubs/{clubId}
 */
@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping
    public ResponseEntity createClub(@RequestBody ClubCreateRequest requestDto) {
        //Validation 및 예외처리 TODO

        Long clubId = clubService.createClub(requestDto);

        //return 처리 schedule controller참고하고 비슷하게
        return null;
    }

//    @DeleteMapping
//    public ResponseEntity deleteClub() {
//        //TODO
//        return null;
//    }
}
