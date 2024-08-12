package GDG.whatssue.domain.user.service;

import GDG.whatssue.domain.user.Error.UserErrorCode;
import GDG.whatssue.domain.user.entity.PhoneCertNum;
import GDG.whatssue.domain.user.repository.PhoneCertNumRepository;
import GDG.whatssue.global.error.CommonException;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
public class MyMessageService {
    private final DefaultMessageService defalutMessageService;
    @Autowired
    private final PhoneCertNumRepository phoneCertNumRepository;

    private String apiKey;
    private String apiSecret;
    private String fromNumber;

    private PhoneCertNum phoneCertNum;

    public MyMessageService(PhoneCertNumRepository certificationNumRepository, @Value("${coolsms.api.key}") String apiKey, @Value("${coolsms.api.secret}") String apiSecret, @Value("${coolsms.api.number}") String fromNumber) {
        this.phoneCertNumRepository = certificationNumRepository;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.fromNumber = fromNumber;
        this.defalutMessageService = NurigoApp.INSTANCE.initialize(apiKey,apiSecret,"https://api.coolsms.co.kr");
        System.out.println("messageService : " + defalutMessageService);
    }
    /*
      단일 메시지 발송 예제
     */
    public SingleMessageSentResponse sendOne(String toNumber, Long userId) {

        Message message = new Message();

        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.

        int randomNum = randomNumber();
        message.setFrom(fromNumber);
        message.setTo(toNumber);
        message.setText("[왓슈]\n 인증번호: " + randomNum + "\n 인증 번호를 정확히 입력해주세요.");

        phoneCertNum = PhoneCertNum.builder()
                .Id(toNumber +":"+ userId)
                .certificationNum(randomNum)
                .createdAt(java.time.LocalDateTime.now())
                .build();

        log.info("PhoneCertNum 확인:" + phoneCertNum.getId());

        phoneCertNumRepository.save(phoneCertNum);

        if (phoneCertNumRepository.findById(phoneCertNum.getId()).isEmpty())
            throw new CommonException(UserErrorCode.EX1103);
            SingleMessageSentResponse response = this.defalutMessageService.sendOne(new SingleMessageSendingRequest(message));

            log.info("response: " + response);

            return response;
        }

        public void checkCertNum(String toNumber, int certNum,Long userId) {
            PhoneCertNum phoneCertNum = phoneCertNumRepository.findById(toNumber + ":" + userId).orElseThrow(() -> new CommonException(UserErrorCode.EX1101));
            if (phoneCertNum.getCertificationNum() != certNum) {
                throw new CommonException(UserErrorCode.EX1102);
            }
        }

        private int randomNumber() {
            Random random = new Random();
            int randomNum = random.nextInt(899) + 100;
            return randomNum;
        }
}

