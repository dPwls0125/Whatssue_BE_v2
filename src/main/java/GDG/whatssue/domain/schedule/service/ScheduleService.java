package GDG.whatssue.domain.schedule.service;

import GDG.whatssue.domain.schedule.dto.AddScheduleRequest;
import GDG.whatssue.domain.schedule.dto.GetScheduleDetailResponse;
import GDG.whatssue.domain.schedule.dto.GetScheduleListResponse;
import GDG.whatssue.domain.schedule.dto.ModifyScheduleRequest;
import java.util.List;

public interface ScheduleService {
    void saveSchedule(Long clubId, Long memberId, AddScheduleRequest requestDto);
    void updateSchedule(Long scheduleId, ModifyScheduleRequest requestDto);
    void deleteSchedule(Long scheduleId);
    GetScheduleDetailResponse findSchedule(Long scheduleId);
    List<GetScheduleListResponse> findScheduleAll(Long clubId);
    List<GetScheduleListResponse> findScheduleByDay(Long clubId, String date);
    List<GetScheduleListResponse> findScheduleByMonth(Long clubId, String date);
    boolean isClubSchedule(Long clubId, Long scheduleId);
}
