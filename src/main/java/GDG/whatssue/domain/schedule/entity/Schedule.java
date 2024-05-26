package GDG.whatssue.domain.schedule.entity;

import GDG.whatssue.domain.attendance.Error.AttendanceErrorCode;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest;
import GDG.whatssue.global.common.BaseEntity;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.attendance.entity.ScheduleAttendanceResult;
import GDG.whatssue.global.error.CommonException;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
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
    private LocalDate scheduleDate;

    @Column(nullable = false)
    private LocalTime scheduleTime;

    @Column(nullable = false)
    private String schedulePlace;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus attendanceStatus;

    @OneToMany(mappedBy = "schedule")
    private List<ScheduleAttendanceResult> attendanceResultList = new ArrayList<>();

    @OneToMany(mappedBy = "schedule")
    private List<OfficialAbsenceRequest> OfficialAbsenceRequestList = new ArrayList<>();

    //==연관관계 메서드==//
    private void setClub(Club club) {
        this.club = club;
        club.getScheduleList().add(this);
    }

    //==생성 메서드==//
    private Schedule(Club club, ClubMember register, String scheduleName, String scheduleContent,
        LocalDate scheduleDate, LocalTime scheduleTime, String schedulePlace) {

        setClub(club);
        this.register = register;
        this.scheduleName = scheduleName;
        this.scheduleContent = scheduleContent;
        this.scheduleDate = scheduleDate;
        this.scheduleTime = scheduleTime;
        this.schedulePlace = schedulePlace;
        this.attendanceStatus = AttendanceStatus.BEFORE;
    }

    /**
     * 정적 팩토리 메서드 패턴
     */
    public static Schedule createSchedule(Club club, ClubMember register, String scheduleName,
        String scheduleContent, LocalDate scheduleDate, LocalTime scheduleTime, String schedulePlace) {

        return new Schedule(club, register, scheduleName, scheduleContent, scheduleDate, scheduleTime, schedulePlace);
    }

    //==비즈니스 로직==//
    /**
     * 일정 출석 시작
     */
    public void checkIsAttendanced() {
        if (this.attendanceStatus == AttendanceStatus.ONGOING) {
            throw new CommonException(AttendanceErrorCode.ATTENDANCE_ALREADY_ONGOING);
        }

        if (this.attendanceStatus == AttendanceStatus.COMPLETE) {
            throw new CommonException(AttendanceErrorCode.ATTENDANCE_ALREADY_COMPLETED);
        }

        this.attendanceStatus = AttendanceStatus.ONGOING;
    }

    /**
     * 일정 출석 종료
     */
    public void finishAttendance() {
        if (this.attendanceStatus != AttendanceStatus.ONGOING) {
            //출석 진행중이 아님 예외
        }
        this.attendanceStatus = AttendanceStatus.COMPLETE;
    }

    /**
     * 일정 내용 수정
     */
    public void update(String scheduleName, String scheduleContent, LocalDate scheduleDate, LocalTime scheduleTime, String schedulePlace) {
        this.scheduleName = scheduleName;
        this.scheduleContent = scheduleContent;
        this.scheduleDate = scheduleDate;
        this.scheduleTime = scheduleTime;
        this.schedulePlace = schedulePlace;
    }
}
