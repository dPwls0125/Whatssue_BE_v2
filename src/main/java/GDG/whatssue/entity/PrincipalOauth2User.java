//package GDG.whatssue.entity;
//
//import lombok.Getter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//
//@Getter
//public class PrincipalOauth2User implements OAuth2User {
//    private User user;
//    public PrincipalOauth2User(User user) {
//        this.user = user;
//        this.user.getClubMemberList().size();
//    }
//    @Override
//    public Map<String, Object> getAttributes() {
//        return null;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<ClubMember> clubMemberList = user.getClubMemberList();
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        for (ClubMember clubMember : clubMemberList) {
//            authorities.add((GrantedAuthority) () -> {
//                Long clubId = clubMember.getClub().getId();
//                Role role = clubMember.getRole();
//                System.out.println("ROLE_" + clubId + role);
//                return "ROLE_" + clubId + role;
//            });
//        }
//        return authorities;
//    }
//
//    @Override
//    public String getName() {
//        return null;
//    }
//}
