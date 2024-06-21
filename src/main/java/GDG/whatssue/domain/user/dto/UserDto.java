package GDG.whatssue.domain.user.dto;

import GDG.whatssue.domain.member.entity.Role;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserDto {
    private Long userId;
    private String userName;
    private String userPhone;
    private String userEmail;
    private String oauth2Id;
}
