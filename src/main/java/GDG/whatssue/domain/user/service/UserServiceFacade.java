package GDG.whatssue.domain.user.service;

import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.user.Error.UserErrorCode;
import GDG.whatssue.domain.user.dto.GetJoinClubResponse;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.domain.user.repository.UserRepository;
import GDG.whatssue.global.error.CommonException;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceFacade {

    private final ClubMemberRepository clubMemberRepository;

    private final UserRepository userRepository;

    public Page<GetJoinClubResponse> getJoinClubList(Long userId, Pageable pageable) {
        return clubMemberRepository.getJoinClubList(userId, pageable);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new CommonException(UserErrorCode.EX1100)
        );
    }





}
