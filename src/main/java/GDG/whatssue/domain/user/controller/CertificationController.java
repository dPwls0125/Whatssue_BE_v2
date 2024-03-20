package GDG.whatssue.domain.user.controller;

import GDG.whatssue.domain.user.service.MyMessageService;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.MessageService;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/certification")
@RestController
@Slf4j
@RequiredArgsConstructor
public class CertificationController {


    private final MyMessageService messageService;

//    public CertificationController(MessageService messageService) {
//        this.messageService = messageService;
//    }
    @PostMapping("/send-one")
    @Operation(summary = "단일 메시지 발송")
    public SingleMessageSentResponse sendOne() {
            return null;
    }

}

