//package GDG.whatssue.service;
//
//import GDG.whatssue.entity.PrincipalOauth2User;
//import GDG.whatssue.repository.UserRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class UserOauth2Service extends DefaultOAuth2UserService {
//    private final UserService userService;
//    private final UserRepository userRepository;
//
//    @Override
//    public PrincipalOauth2User loadUser(OAuth2UserRequest userRequest) {
////        String name = oAuth2User.getName();
//        return new PrincipalOauth2User(null);
//    }
//}
