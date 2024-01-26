package GDG.whatssue.service;

import GDG.whatssue.dto.schedule.AttendanceNumResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.FileAlreadyExistsException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AttendanceService  {
    private static Map<Long,Map<Long,Integer>> attendanceNumMap = new HashMap<>();
    public AttendanceNumResponseDto openAttendance(Long clubId, Long scheduleId) throws Exception{
        Random random = new Random();
        int randomInt = random.nextInt(1,1001);
        if(attendanceNumMap.containsKey(clubId)) {
            if(attendanceNumMap.get(clubId).containsKey(scheduleId))
                    throw new Exception("이미 출석이 진행중입니다.");
            }else{
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
}
