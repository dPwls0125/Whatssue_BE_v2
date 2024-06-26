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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    @Value("${front.url}")
    private String frontUrl;
    /*
    Third party 접근을 위한 accessToken 발급 이후 실행됨
     */

    private User findOrSaveUser(OAuth2User oAuth2User, String registrationId, String name) {
        String oauth2Id = registrationId + ":" + oAuth2User.getName(); // name = ID값
        // 임시 유저로 역할 설정
//        Role role = roleRepository.findByName(RoleName.TEMPORARY)
//                .orElseThrow(() -> new OAuth2AuthenticationException("존재하지 않는 권한입니다."));

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
        for (ClubMember clubMember : clubMemberList) {
            authorities.add((GrantedAuthority) () -> {
                Long clubId = clubMember.getClub().getId();
                Role role = clubMember.getRole();
                System.out.println("ROLE_" + clubId + role);
                return "ROLE_" + clubId + role;
            });
        }
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

    public UserDto getUserInfo(KakaoDetails kakaoDetails) {
        User user = kakaoDetails.getUser();
        Long userId = user.getUserId();
        user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
         // user정보를 Repository에서 가져와야 signup 이후의 정보(phone, name)를 가져올 수 있음.
        UserDto dto = UserDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .oauth2Id(user.getOauth2Id())
                .userPhone(user.getUserPhone())
                .userEmail(user.getUserEmail())
                .build();
        return dto;
    }
    public UserDto signUp(KakaoDetails kakaoDetails, SignUpRequestDto request){
        User user = kakaoDetails.getUser();
        user.setUserPhone(request.getUserPhone());
        user.setUserName(request.getUserName());
        user.setUserEmail(request.getUserEmail());
        userRepository.save(user);
        return UserDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .oauth2Id(user.getOauth2Id())
                .userEmail(user.getUserEmail())
                .userPhone(request.getUserPhone())
                .build();
    }

    public UserDto modifyUserInfo(KakaoDetails kakaoDetails, UserDto request){
        User user = kakaoDetails.getUser();
        if(request.getUserId() != user.getUserId()){
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        user = User.builder()
                .userId(user.getUserId())
                .userPhone(request.getUserPhone())
                .userName(request.getUserName())
                .userEmail(request.getUserEmail())
                .oauth2Id(user.getOauth2Id())
                .build();
        userRepository.save(user);
        UserDto dto = UserDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .oauth2Id(user.getOauth2Id())
                .userPhone(user.getUserPhone())
                .build();
        return dto;
    }

    public RedirectView loginRedirect(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        RedirectView redirectView = new RedirectView();
        if(user.getUserPhone()==null || user.getUserEmail()==null){
            System.out.println("회원가입 안되어있음");
            redirectView.setUrl( frontUrl + "/user/login"); // 회원가입 페이지
        } else {
            System.out.println("회원가입 되어있음");
            redirectView.setUrl(frontUrl);// 메인 페이지
        }
        return redirectView;
    }

}
