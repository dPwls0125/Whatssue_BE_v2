package GDG.whatssue.domain.user.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDto {
    private String userId;
    private String userName;
    private String userPhone;
    private String role;
    private String oauth2Id;
}
