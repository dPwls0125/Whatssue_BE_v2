package GDG.whatssue.domain.user.controller;

import GDG.whatssue.domain.user.entity.KakaoDetails;
import GDG.whatssue.domain.user.service.MyMessageService;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/certification")
@RestController
@Slf4j
@RequiredArgsConstructor
public class CertificationController {
    private final MyMessageService myMessageService;
    @PostMapping("/send-random-number")
    @Operation(summary = "인증 번호 문자 메세지 발송")
    public ResponseEntity sendOne(@RequestParam("toNumber") String toNumber, @AuthenticationPrincipal KakaoDetails kakaoDetails) {
        Long userId = kakaoDetails.getUser().getUserId();
        SingleMessageSentResponse response = myMessageService.sendOne(toNumber, userId);
        return ResponseEntity.status(200).body(response);
    }
    @GetMapping("/check-random-number")
    @Operation(summary = "인증 번호 확인")
    public ResponseEntity<Void> checkRandomNumber(@RequestParam("toNumber") String toNumber, @RequestParam("certificationNum") int certificationNum, @AuthenticationPrincipal KakaoDetails kakaoDetails) {
        Long userId = kakaoDetails.getUser().getUserId();
<<<<<<< Updated upstream
        Boolean result = myMessageService.checkCertNum(toNumber, certificationNum, userId);
        return ResponseEntity.status(200).body(result);
=======
         myMessageService.checkCertNum(toNumber, certificationNum, userId);

        return ResponseEntity.status(200).build();
>>>>>>> Stashed changes
    }
}

