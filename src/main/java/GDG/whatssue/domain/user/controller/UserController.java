package GDG.whatssue.domain.user.controller;

import GDG.whatssue.domain.user.dto.SignUpRequestDto;
import GDG.whatssue.domain.user.dto.UserDto;
//import GDG.whatssue.domain.user.entity.PrincipalDetails;
//import GDG.whatssue.domain.user.service.UserService;
import GDG.whatssue.domain.user.entity.KakaoDetails;
import GDG.whatssue.domain.user.service.CustomOauth2Service;
import GDG.whatssue.global.common.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class  UserController {

    private final CustomOauth2Service customOauth2Service;

    @GetMapping("/getInfo")
    public ResponseEntity getUserProfile(@AuthenticationPrincipal KakaoDetails kakaoDetails) {
        UserDto userDto  = customOauth2Service.getUserInfo(kakaoDetails);
        return ResponseEntity.status(200).body(userDto);
    }

    @PostMapping ("/signUp")
    public ResponseEntity signUpWithPhoneNumAndName(@AuthenticationPrincipal KakaoDetails kakaoDetails, SignUpRequestDto request) {
        UserDto userDto = customOauth2Service.signUp(kakaoDetails,request);
        return ResponseEntity.status(200).body(userDto);
    }

    @PostMapping("/modification")
    public ResponseEntity modifyUserInfo(@AuthenticationPrincipal KakaoDetails kakaoDetails, UserDto request) {
        UserDto userDto = customOauth2Service.modifyUserInfo(kakaoDetails,request);
        return ResponseEntity.status(200).body(userDto);
    }

    @GetMapping("/login/redirect")
    @Operation(summary = "로그인 이후 적절한 화면으로 리다이렉트", description = "회원가입을 안한 유저인 경우 -> signup page\n 회원가입을 한 유저인 경우 -> main page")
    public RedirectView loginRedirect(@LoginUser Long userId){
        RedirectView redirect = customOauth2Service.loginRedirect(userId);
        return redirect;
    }
}
