package GDG.whatssue.domain.officialabsence.entity;

import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import lombok.Setter;

@Getter
@Entity
@Builder
@Setter
@AllArgsConstructor
public class OfficialAbsenceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "official_absence_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "club_member_id", nullable = false)
    private ClubMember clubMember;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @JoinColumn(name = "official_absence_content", nullable = false)
    private String officialAbsenceContent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfficialAbsenceRequestType officialAbsenceRequestType;

    public OfficialAbsenceRequest() {
        // 기본 생성자
    }
}
