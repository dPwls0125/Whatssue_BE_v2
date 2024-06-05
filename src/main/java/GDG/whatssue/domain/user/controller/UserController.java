package GDG.whatssue.domain.user.controller;

import GDG.whatssue.domain.user.dto.SignUpRequestDto;
import GDG.whatssue.domain.user.dto.UserDto;
import GDG.whatssue.domain.user.dto.UserModifiyRequestDto;
import GDG.whatssue.domain.user.service.CustomOauth2Service;
import GDG.whatssue.domain.user.service.UserService;
import GDG.whatssue.domain.user.service.UserServiceFacade;
import GDG.whatssue.global.common.annotation.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class  UserController {

    private final UserServiceFacade userServiceFacade;
    private final CustomOauth2Service customOauth2Service;
    private final UserService userService;

    @GetMapping("/getInfo")
    public ResponseEntity getUserProfile(@LoginUser Long userId) {
        UserDto userDto  = userService.getUserInfo(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @PostMapping ("/signUp")
    public ResponseEntity signUpWithPhoneNumAndName(@LoginUser Long userId, @RequestBody SignUpRequestDto request) {
        UserDto userDto = userService.signUp(userId,request);
        return ResponseEntity.status(200).body(userDto);
    }

    @PostMapping("/modification/{modifierId}")
    public ResponseEntity modifyUserInfo(@LoginUser Long userId, @PathVariable("modifierId") Long modifierId , @RequestBody UserModifiyRequestDto request) {
        UserDto userDto = userService.modifyUserInfo(userId,modifierId,request);
        return ResponseEntity.status(200).body(userDto);
    }

}
