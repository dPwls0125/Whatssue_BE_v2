package GDG.whatssue.domain.user.service;

import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.user.dto.SignUpRequestDto;
import GDG.whatssue.domain.user.dto.UserDto;
import GDG.whatssue.domain.user.entity.KakaoDetails;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.domain.user.repository.UserRepository;
import GDG.whatssue.domain.member.entity.Role;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Getter
public class CustomOauth2Service extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    /*
    Third party 접근을 위한 accessToken 발급 이후 실행됨
     */
    private User findOrSaveUser(OAuth2User oAuth2User, String registrationId, String name) {
        String oauth2Id = registrationId + ":" + oAuth2User.getName(); // name = ID값

        return userRepository.findByOauth2Id(oauth2Id)
                .orElseGet(() -> userRepository.save(User.builder()
                        .oauth2Id(oauth2Id)
                        .userName(name)
                        .clubMemberList(new ArrayList<>())
                        .clubJoinRequestList(new ArrayList<>())
                        .build()));
    }

    @Override
    public KakaoDetails loadUser(OAuth2UserRequest userRequest) {
        // accessToken으로 서드파티에 요청해서 사용자 정보를 얻어옴

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        User user = findOrSaveUser(oAuth2User, userRequest.getClientRegistration().getRegistrationId(), profile.get("nickname").toString());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        List<ClubMember> clubMemberList = user.getClubMemberList();

//        for (ClubMember clubMember : clubMemberList) {
//            authorities.add((GrantedAuthority) () -> {
//                Long clubId = clubMember.getClub().getId();
//                Role role = clubMember.getRole();
//                System.out.println("ROLE_" + clubId + role);
//                return "ROLE_" + clubId + role;
//            });
//        }

        KakaoDetails kakaoDetails = KakaoDetails.builder()
                .registrationId(userRequest.getClientRegistration().getRegistrationId())
                .user(user)
                .authorities(authorities)
                .attributes(oAuth2User.getAttributes())
                .build();
        return kakaoDetails;
        // oauthPrincipal이 return이 되면 시큐리티 session의 Authentication의 내부에  저장이 된다.
        // Session(내부 Authentication(내부 oauthUserDetails))
    }


    @Bean
    @Profile("test")
    public UserDetailsService userDetailsService() {
        UserDetailsService userDetailsService = username -> new InMemoryUserDetailsManager(
                org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder()
                        .username("testuser")
                        .password("password")
                        .roles("USER")
                        .build()
        ).loadUserByUsername(username);
        return userDetailsService;
    }



}
