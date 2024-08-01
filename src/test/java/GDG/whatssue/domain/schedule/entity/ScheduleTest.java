package GDG.whatssue.domain.schedule.entity;

import static GDG.whatssue.domain.schedule.entity.AttendanceStatus.*;
import static org.assertj.core.api.Assertions.*;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.global.error.CommonException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ScheduleTest {
    @Mock ClubMember clubMember;
    @Mock Club club;

    @Test
    @DisplayName("일정 출석시작 성공")
    void start_attendance_success() {
        //given
        Schedule schedule = createSchedule();

        //when
        schedule.startAttendance();

        //then
        assertThat(schedule.getAttendanceStatus()).isEqualTo(ONGOING);
    }

    @Test
    @DisplayName("일정 출석시작 실패")
    void start_attendance_fail() {
        //given
        Schedule schedule1 = createSchedule();
        schedule1.startAttendance();

        Schedule schedule2 = createSchedule();
        schedule2.startAttendance();
        schedule2.finishAttendance();

        //when, then
        assertThatThrownBy(() -> schedule1.startAttendance()) //이미 진행중
            .isInstanceOf(CommonException.class);

        assertThatThrownBy(() -> schedule2.startAttendance()) //이미 완료
            .isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("일정 출석종료 성공")
    void finish_attendance_success() {
        //given
        Schedule schedule = createSchedule();
        schedule.startAttendance();

        //when
        schedule.finishAttendance();

        //then
        assertThat(schedule.getAttendanceStatus()).isEqualTo(COMPLETE);
    }

    @Test
    @DisplayName("일정 출석종료 실패")
    void finish_attendance_fail() {
        //given
        Schedule schedule1 = createSchedule();

        Schedule schedule2 = createSchedule();
        schedule2.startAttendance();
        schedule2.finishAttendance();

        //when, then
        assertThatThrownBy(() -> schedule1.finishAttendance()) //진행중이 아님
            .isInstanceOf(CommonException.class);

        assertThatThrownBy(() -> schedule2.finishAttendance()) //이미 완료
            .isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("일정 수정 성공")
    void update() {
        //given
        Schedule schedule = createSchedule();
        LocalDate updateDate = LocalDate.now().minusDays(1);
        LocalTime updateTime = LocalTime.now();

        //when
        schedule.update("수정이름", "수정내용", updateDate, updateTime, "수정장소");

        //then
        assertThat(schedule.getScheduleName()).isEqualTo("수정이름");
        assertThat(schedule.getScheduleContent()).isEqualTo("수정내용");
        assertThat(schedule.getScheduleDate()).isEqualTo(LocalDateTime.of(updateDate, updateTime));
        assertThat(schedule.getSchedulePlace()).isEqualTo("수정장소");
    }

    @NotNull
    private Schedule createSchedule() {
        return Schedule.createSchedule(club, clubMember, "테스트일정", "테스트입니다",
            LocalDate.now(), LocalTime.now(), "역곡");
    }
}