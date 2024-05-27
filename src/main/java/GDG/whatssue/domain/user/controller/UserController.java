package GDG.whatssue.domain.user.controller;

import GDG.whatssue.domain.user.dto.GetJoinClubResponse;
import GDG.whatssue.domain.user.dto.SignUpRequestDto;
import GDG.whatssue.domain.user.dto.UserDto;
import GDG.whatssue.domain.user.dto.UserModifiyRequestDto;
import GDG.whatssue.domain.user.entity.KakaoDetails;
import GDG.whatssue.domain.user.service.CustomOauth2Service;
import GDG.whatssue.domain.user.service.UserService;
import GDG.whatssue.domain.user.service.UserServiceFacade;
import GDG.whatssue.global.common.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class  UserController {

    private final UserServiceFacade userServiceFacade;
    private final CustomOauth2Service customOauth2Service;
    private final UserService userService;

    @Operation(summary = "가입한 모임 조회")
    @GetMapping("/clubs")
    public ResponseEntity<Page<GetJoinClubResponse>> getJoinClubList(@LoginUser Long userId, Pageable pageable) {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userServiceFacade.getJoinClubList(userId, pageable));
    }

    @GetMapping("/getInfo")
    public ResponseEntity getUserProfile(@LoginUser Long userId) {
        UserDto userDto  = userService.getUserInfo(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @PostMapping ("/signUp")
    public ResponseEntity signUpWithPhoneNumAndName(@LoginUser Long userId, SignUpRequestDto request) {
        UserDto userDto = userService.signUp(userId,request);
        return ResponseEntity.status(200).body(userDto);
    }

    @PostMapping("/modification/{modifierId}")
    public ResponseEntity modifyUserInfo(@LoginUser Long userId, @PathVariable("modifierId") Long modifierId , UserModifiyRequestDto request) {
        UserDto userDto = userService.modifyUserInfo(userId,modifierId,request);
        return ResponseEntity.status(200).body(userDto);
    }

}
