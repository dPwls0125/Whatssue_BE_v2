package GDG.whatssue.domain.schedule.entity;

import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest;
import GDG.whatssue.domain.schedule.dto.ModifyScheduleRequest;
import GDG.whatssue.global.common.BaseEntity;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.attendance.entity.ScheduleAttendanceResult;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_member_id", nullable = false)
    private ClubMember clubMember;

    @Column(nullable = false)
    private String scheduleName;

    @Column
    private String scheduleContent;

    @Column(nullable = false)
    private LocalDateTime scheduleDateTime;
    
    @Column(nullable = false)
    private String schedulePlace;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus attendanceStatus;

    @OneToMany(mappedBy = "schedule")
    private List<ScheduleAttendanceResult> attendanceResultList = new ArrayList<>();

    @OneToMany(mappedBy = "schedule")
    private List<OfficialAbsenceRequest> OfficialAbsenceRequestList = new ArrayList<>();

    public void update(ModifyScheduleRequest request) {
        this.scheduleName = request.getScheduleName();
        this.scheduleContent = request.getScheduleContent();
        this.scheduleDateTime = request.getScheduleDateTime();
        this.schedulePlace = request.getSchedulePlace();
    }

    @Builder
    public Schedule(Club club, ClubMember clubMember, String scheduleName, String scheduleContent,
        LocalDateTime scheduleDateTime, String schedulePlace, AttendanceStatus attendanceStatus) {
        this.club = club;
        this.clubMember = clubMember;
        this.scheduleName = scheduleName;
        this.scheduleContent = scheduleContent;
        this.scheduleDateTime = scheduleDateTime;
        this.schedulePlace = schedulePlace;
        this.attendanceStatus = attendanceStatus;
    }
}
