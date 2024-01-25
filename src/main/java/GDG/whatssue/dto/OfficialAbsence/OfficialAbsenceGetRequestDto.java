package GDG.whatssue.dto.OfficialAbsence;

import GDG.whatssue.entity.ClubMember;
import GDG.whatssue.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class OfficialAbsenceGetRequestDto {

    private Long id;
    private ClubMember clubMember;
    private Schedule schedule;
    private String officialAbsenceContent;

}

