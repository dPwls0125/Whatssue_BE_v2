package GDG.whatssue.domain.member.dto;

import GDG.whatssue.domain.member.entity.Role;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ClubMemberDto {

    private Long memberId;
    private Role role;
}
