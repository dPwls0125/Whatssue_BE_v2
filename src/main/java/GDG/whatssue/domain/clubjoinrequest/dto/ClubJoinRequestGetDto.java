package GDG.whatssue.domain.clubjoinrequest.dto;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ClubJoinRequestGetDto {
    private Long id;
    private Long clubId;
    private Long userId;
    private String userName;
    private LocalDateTime createdAt;
}
