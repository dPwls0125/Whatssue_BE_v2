//package GDG.whatssue.controller;
//
//import GDG.whatssue.domain.club.entity.Club;
//import java.time.LocalTime;
//import GDG.whatssue.domain.club.entity.NamePolicy;
//import GDG.whatssue.domain.club.repository.ClubRepository;
//import GDG.whatssue.domain.member.entity.ClubMember;
//import GDG.whatssue.domain.member.entity.Role;
//import GDG.whatssue.domain.member.repository.ClubMemberRepository;
//import GDG.whatssue.domain.schedule.entity.AttendanceStatus;
//import GDG.whatssue.domain.schedule.entity.Schedule;
//import GDG.whatssue.domain.schedule.repository.ScheduleRepository;
//import GDG.whatssue.domain.user.entity.User;
//import GDG.whatssue.domain.user.repository.UserRepository;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestReporter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@Transactional
//@AutoConfigureMockMvc
//@DisplayName("출석 컨트롤러 테스트")
//@WithMockUser(username = "testUser", roles = "USER")
//@Disabled
//public class AttendanceControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ClubRepository clubRepository;
//    @Autowired
//    private ClubMemberRepository clubMemberRepository;
//    @Autowired
//    private ScheduleRepository scheduleRepository;
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    @DisplayName("출석 열기")
//    public void openAttendance(TestReporter testReporter) throws Exception {
//        //given
//        Club club = Club.builder()
//                .id(1L)
//                .clubName("GDG")
//                .clubIntro("GDG는 개발자들의 모임입니다.")
//                .isPrivate(false)
//                .namePolicy(NamePolicy.NICK_NAME)
//                .contactMeans("010-1234-5678")
//                .privateCode("1234")
//                .profileImage(null)
//                .build();
//
//        clubRepository.save(club);
//
//        User user = User.builder()
//                .userId(1L)
//                .userEmail("kimphoby0125@gmail.com")
//                .userName("김포비")
//                .userPhone("010-1234-5678")
//                .oauth2Id("1234")
//                .build();
//
//        userRepository.save(user);
//
//        ClubMember clubMember = ClubMember.builder()
//                .isFirstVisit(false)
//                .club(club)
//                .id(1L)
//                .role(Role.MANAGER)
//                .user(user)
//                .build();
//
//        clubMemberRepository.save(clubMember);
//
//        Schedule schedule = Schedule.builder()
//                .id(1L)
//                .club(club)
//                .attendanceStatus(AttendanceStatus.BEFORE)
//                .scheduleName("니똥")
//                .register(clubMember)
//                .scheduleContent("니똥이랑 놀기")
//                .scheduleDate(LocalDate.now())
//                .schedulePlace("강남역")
//                .scheduleTime(LocalTime.now())
//                .build();
//
//        scheduleRepository.save(schedule);
//        //when, then
//        testReporter.publishEntry(mockMvc.perform(
//                get("/api/{clubId}/schedules/{scheduleId}/attendance-start", club.getId(), schedule.getId()))
//                        .andExpect(status().isOk())
//                        .andReturn().toString());
//    }
//
//}
