package GDG.whatssue.service;

import GDG.whatssue.dto.Attendance.AttendanceNumResponseDto;
import GDG.whatssue.dto.Attendance.ScheduleAttendanceMemberDto;
import GDG.whatssue.entity.ScheduleAttendanceResult;
import GDG.whatssue.repository.ScheduleAttendanceResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private static Map<Long, Map<Long, Integer>> attendanceNumMap = new HashMap<>();
    private final ScheduleAttendanceResultRepository scheduleAttendanceResultRepository;

    public AttendanceNumResponseDto openAttendance(Long clubId, Long scheduleId) throws Exception {
        Random random = new Random();
        int randomInt = random.nextInt(1, 1001);
        if (attendanceNumMap.containsKey(clubId)) {
            if (attendanceNumMap.get(clubId).containsKey(scheduleId))
                throw new Exception("이미 출석이 진행중입니다.");
        } else {
            attendanceNumMap.put(clubId, new HashMap<>());
        }
        Map<Long, Integer> innerMap = attendanceNumMap.get(clubId);
        innerMap.put(scheduleId, randomInt);
        randomInt = attendanceNumMap.get(clubId).get(scheduleId);
        AttendanceNumResponseDto attendanceNumResponseDto = AttendanceNumResponseDto.builder()
                .AttendanceNum(randomInt)
                .clubId(clubId)
                .scheduleId(scheduleId)
                .build();
        return attendanceNumResponseDto;
    }

    public void deleteAttendance(Long clubId, Long scheduleId) throws Exception {
        if (attendanceNumMap.containsKey(clubId)) {
            if (attendanceNumMap.get(clubId).containsKey(scheduleId))
                attendanceNumMap.get(clubId).remove(scheduleId);
            else
                throw new Exception("출석이 진행중이지 않습니다.");
        } else {
            throw new Exception("출석이 진행중이지 않습니다.");
        }
    }
    public List<ScheduleAttendanceMemberDto> getAttendanceList(Long scheduleId, Long clubId)throws Exception {
        List<ScheduleAttendanceResult> attendanceList;
        attendanceList = scheduleAttendanceResultRepository.findByScheduleId(scheduleId);

        if(attendanceList.isEmpty()) throw new Exception("출석한 멤버가 존재하지 않습니다.");

        List<ScheduleAttendanceMemberDto>attendedMembers = attendanceList.stream().map(m -> {
            if(m.getAttendanceType().toString().equals("ATTENDANCE")) {
                return ScheduleAttendanceMemberDto.builder()
                        .clubId(clubId)
                        .scheduleId(scheduleId)
                        .clubMemberId(m.getClubMember().getId())
                        .attendanceType(m.getAttendanceType())
                        .build();
            }
            else return null;
        }).filter(Objects::nonNull).collect(Collectors.toList()).reversed();

        return attendedMembers;
    }
}
