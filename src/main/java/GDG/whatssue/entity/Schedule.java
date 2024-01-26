package GDG.whatssue.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Column(nullable = false)
    private String scheduleName;

    @Column(nullable = false)
    private String scheduleContent;

    @Column(nullable = false)
    private LocalDate scheduleDate;

    @Column(nullable = false)
    private LocalTime scheduleTime;
    
    @Column(nullable = false)
    private boolean isChecked;

    @OneToMany(mappedBy = "schedule")
    private List<ScheduleAttendanceResult> attendanceResultList;

    @OneToMany(mappedBy = "schedule")
    private List<OfficialAbsenceRequest> officialAbsenceRequestList;
}
