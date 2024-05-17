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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
<<<<<<< Updated upstream
=======
import lombok.NoArgsConstructor;
>>>>>>> Stashed changes

@Getter
@Entity
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)//지연 로딩 설정
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY) //지연 로딩 설정
    @JoinColumn(name = "club_member_id", nullable = false)
    private ClubMember register;

    @Column(nullable = false)
    private String scheduleName;

    @Column(nullable = false)
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

    //==생성 메서드==//
    private Schedule() {
    }

    private Schedule(Club club, ClubMember register, String scheduleName, String scheduleContent,
        LocalDateTime scheduleDateTime, String schedulePlace) {
        this.club = club;
        club.getScheduleList().add(this); //연관관계 편의 메서드

        this.register = register;
        this.scheduleName = scheduleName;
        this.scheduleContent = scheduleContent;
        this.scheduleDateTime = scheduleDateTime;
        this.schedulePlace = schedulePlace;
        this.attendanceStatus = AttendanceStatus.BEFORE;
    }

    /**
     * 정적 팩토리 메서드 패턴
     */
    public static Schedule of(Club club, ClubMember register, String scheduleName,
        String scheduleContent, LocalDateTime scheduleDateTime, String schedulePlace) {

        return new Schedule(club, register, scheduleName, scheduleContent, scheduleDateTime, schedulePlace);
    }

    //==비즈니스 로직==//
    /**
     * 일정 출석 시작
     */
    public void startAttendance() {
        if (attendanceStatus == AttendanceStatus.ONGOING) {
            //이미 출석 진행중 예외
        }

        if (attendanceStatus == AttendanceStatus.COMPLETE) {
            //이미 출석 완료 예외
        }

        attendanceStatus = AttendanceStatus.ONGOING;
    }

    /**
     * 일정 출석 종료
     */
    public void finishAttendance() {
        if (attendanceStatus != AttendanceStatus.ONGOING) {
            //출석 진행중이 아님 예외
        }
        attendanceStatus = AttendanceStatus.COMPLETE;
    }

    /**
     * 일정 내용 수정
     */
    public void update(ModifyScheduleRequest request) {
        this.scheduleName = request.getScheduleName();
        this.scheduleContent = request.getScheduleContent();
        this.scheduleDateTime = request.getScheduleDateTime();
        this.schedulePlace = request.getSchedulePlace();
    }
}
