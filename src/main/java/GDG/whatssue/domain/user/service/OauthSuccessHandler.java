package GDG.whatssue.domain.user.service;

import GDG.whatssue.domain.user.Error.UserErrorCode;
import GDG.whatssue.domain.user.entity.KakaoDetails;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.domain.user.repository.UserRepository;
import GDG.whatssue.global.error.CommonException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
@Component
public class OauthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Value("${front.url}")
    private String frontUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        KakaoDetails kakaoDetails = (KakaoDetails) authentication.getPrincipal();
        Long userId = kakaoDetails.getUser().getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(UserErrorCode.EX1100));

        Collection<String> cookies = response.getHeaders("Set-Cookie");

        cookies.stream()
                .filter(header -> header.contains("JSESSIONID"))
                .map(header -> String.format("%s; %s", header, "SameSite=None; Secure"))
                .forEach(header -> response.setHeader("Set-Cookie", header));

        if( user.getUserPhone()==null || user.getUserEmail()==null ){

            log.info("회원가입 안되어있음 -> 회원가입 페이지로 redirect");
            response.sendRedirect(frontUrl + "/user/signup");
        } else {
            log.info("회원가입 되어 있으므로 메인 페이지로 redirect");
            response.sendRedirect(frontUrl);

        }
    }

}
