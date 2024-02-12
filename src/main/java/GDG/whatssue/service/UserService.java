package GDG.whatssue.service;
import GDG.whatssue.dto.User.UserDto;
import GDG.whatssue.entity.User;
import GDG.whatssue.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public void signUp(UserDto userDto) {
        User user = User.builder()
                .userNick(userDto.getUserNick())
                .userPw(userDto.getUserPw())
                .userEmail(userDto.getUserEmail())
                .userName(userDto.getUserName())
                .userPhone(userDto.getUserPhone())
                .build();
        // 비밀번호 암호화 : 비밀번호 암호화가 안되어있으면 security로 로그인을 할 수 없음.
        user.setUserPw(passwordEncoder.encode(user.getUserPw()));
        userRepository.save(user);
    }
    @Override
    public UserDetails loadUserByUsername(String userNickname) {
        User user = userRepository.findByUserNick(userNickname);

        if(userRepository.findByUserNick(userNickname) == null)
            return null;

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserNick())
                .password(user.getUserPw())
                .roles("USER")
                .build();
        return userDetails;
    }



}
