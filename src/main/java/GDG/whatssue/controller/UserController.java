package GDG.whatssue.controller;

import GDG.whatssue.dto.User.UserDto;
import GDG.whatssue.entity.User;
import GDG.whatssue.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity signUp(UserDto userDto) {
        userService.signUp(userDto);
        return null;
    }

}
