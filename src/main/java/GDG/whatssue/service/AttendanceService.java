package GDG.whatssue.service;

import GDG.whatssue.dto.schedule.AttendanceNumResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private static Map<Long,Map<Long,Integer>> attendanceNumMap = new HashMap<>();
    public AttendanceNumResponseDto openAttendance(Long clubId, Long scheduleId){
        Random random = new Random();
        int randomInt = random.nextInt(1,1001);
        try {
            if (!attendanceNumMap.containsKey(clubId)) {
                attendanceNumMap.put(clubId, new HashMap<>());
            }
            Map<Long, Integer> innerMap = attendanceNumMap.get(clubId);
            innerMap.put(scheduleId, randomInt);
            randomInt = attendanceNumMap.get(clubId).get(scheduleId);
        }catch(Exception e){
            System.out.println("예외 발생 : " + e);
        }
        AttendanceNumResponseDto attendanceNumResponseDto = AttendanceNumResponseDto.builder()
                .AttendanceNum(randomInt)
                .clubId(clubId)
                .scheduleId(scheduleId)
                .build();
        return attendanceNumResponseDto;
    }
}
