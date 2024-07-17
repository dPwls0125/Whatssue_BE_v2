package GDG.whatssue.domain.member.service;


import GDG.whatssue.domain.member.dto.MemberRoleRequest;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.entity.Role;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static GDG.whatssue.domain.member.entity.Role.MANAGER;
import static GDG.whatssue.domain.member.entity.Role.MEMBER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubMemberManagingService {

    private final ClubMemberRepository clubMemberRepository;

    public void deleteClubMember(Long memberId) {

        ClubMember clubMember = clubMemberRepository.findById(memberId)
            .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100));

        clubMemberRepository.delete(clubMember);
    }

    @Transactional
    public void modifyClubMemberRole(MemberRoleRequest request) {

        ClubMember clubMember = clubMemberRepository.findById(request.getMemberId())
            .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100));

        if (request.getRole() == MANAGER)
            clubMember.switchToManager();

        else if (request.getRole() == MEMBER)
            clubMember.switchToMember();

        clubMemberRepository.save(clubMember);
    }

}
