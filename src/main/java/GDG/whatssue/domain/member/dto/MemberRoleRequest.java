package GDG.whatssue.domain.member.dto;

import GDG.whatssue.domain.member.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRoleRequest {

    private Long memberId;

    private Role role;

}
