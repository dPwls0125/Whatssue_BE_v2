package GDG.whatssue.domain.club.dto;

import lombok.Getter;

@Getter
public class ClubUpdateRequest {

    String clubName;
    String clubIntro;
    Boolean isPrivate;
    String contactMeans;
}
