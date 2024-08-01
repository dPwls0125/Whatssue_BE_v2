package GDG.whatssue.domain.user.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@Getter
public class UserModifiyRequestDto {

    private String userName;
    private String userPhone;
    private String userEmail;

}


