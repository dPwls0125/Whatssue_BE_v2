package GDG.whatssue.domain.attendance.service;

import GDG.whatssue.domain.attendance.dto.AttendanceNumRequestDto;
import GDG.whatssue.domain.attendance.dto.AttendanceNumResponseDto;
import GDG.whatssue.domain.attendance.dto.ScheduleAttendanceMemberDto;
import GDG.whatssue.domain.attendance.dto.ScheduleDto;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequestType;
import GDG.whatssue.domain.officialabsence.repository.OfficialAbsenceRequestRepository;
import GDG.whatssue.domain.schedule.entity.Schedule;
import GDG.whatssue.domain.attendance.entity.AttendanceType;
import GDG.whatssue.domain.attendance.entity.ScheduleAttendanceResult;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.attendance.repository.ScheduleAttendanceResultRepository;
import GDG.whatssue.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private static Map<Long, Map<Long, Integer>> attendanceNumMap = new HashMap<>();
    private final ScheduleAttendanceResultRepository scheduleAttendanceResultRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ScheduleRepository scheduleRepository;
    private final OfficialAbsenceRequestRepository officialAbsenceRequestRepository;

    public AttendanceNumResponseDto openAttendance(Long clubId, Long scheduleId) throws Exception {

        if(!scheduleRepository.findById(scheduleId).get().isChecked()){
//            if(scheduleAttendanceResultRepository.findById(scheduleId).isPresent()) {
//                throw new Exception("이미 출석이 진행중입니다. 출석을 종료한 후에 다시 시도해 주세요.");
//            }
            List<ClubMember> clubMembers = clubMemberRepository.findByClubId(clubId).orElseThrow(() -> new Exception("해당 동아리에 가입된 멤버가 없습니다."));
            // 해당 출석을 여는
            for(ClubMember clubMember : clubMembers){
                ScheduleAttendanceResult scheduleAttendanceResult = ScheduleAttendanceResult.builder()
                        .clubMember(clubMember)
                        .schedule(scheduleRepository.findById(scheduleId).orElseThrow(() -> new Exception("해당 일정이 존재하지 않습니다.")))
                        .attendanceType(AttendanceType.ABSENCE)
                        .build();
                scheduleAttendanceResultRepository.save(scheduleAttendanceResult);
            }
        }

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
    //현재 진행중인 일정 리스트
    public List<ScheduleDto> currentAttendanceList(Long clubId) {
        List<ScheduleDto> scheduleIdList = new ArrayList<>();
        if (attendanceNumMap.containsKey(clubId)) {
            for (Long scheduleId : attendanceNumMap.get(clubId).keySet()) {
                Schedule schedule = scheduleRepository.findById(scheduleId).get();
                ScheduleDto dto = ScheduleDto.builder()
                        .scheduleId(schedule.getId())
                        .clubId(schedule.getClub().getId())
                        .scheduleName(schedule.getScheduleName())
                        .scheduleContent(schedule.getScheduleContent())
                        .scheduleDateTime(schedule.getScheduleDateTime())
                        .isChecked(schedule.isChecked())
                        .build();
                scheduleIdList.add(dto);
            }
        }
        return scheduleIdList;
    }
    /*Delete 시에 결석자 명단을 업로드해야할까?*/
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
    public List<ScheduleAttendanceMemberDto> getAttendanceList(Long scheduleId, Long clubId) throws Exception {
        List<ScheduleAttendanceResult> attendanceList;
        attendanceList = scheduleAttendanceResultRepository.findByScheduleId(scheduleId);

        if (attendanceList.isEmpty()) throw new Exception("출석한 멤버가 존재하지 않습니다.");
        List<ScheduleAttendanceMemberDto> attendedMembers = (List<ScheduleAttendanceMemberDto>) attendanceList.stream().map(m -> {
            if (m.getAttendanceType().toString().equals("ATTENDANCE")) {
                return ScheduleAttendanceMemberDto.builder()
                        .clubId(clubId)
                        .scheduleId(scheduleId)
                        .clubMemberId(m.getClubMember().getId())
                        .attendanceType(m.getAttendanceType())
                        .build();
            } else return null;
        });
        return attendedMembers;
    }
    public void doAttendance(Long clubId, Long schduleId, Long memberId, AttendanceNumRequestDto requestDto) throws Exception{

        int attendanceNum = attendanceNumMap.get(clubId).get(schduleId);
        int inputValue = requestDto.getAttendanceNum();

        if (attendanceNum == inputValue) {
            ScheduleAttendanceResult scheduleAttendanceResult = scheduleAttendanceResultRepository.findByScheduleIdAndClubMemberId(schduleId, memberId)
                    .orElseThrow(() -> new Exception("해당 일정에 대한 출석 결과가 존재하지 않습니다."));
            scheduleAttendanceResult.setAttendanceType(AttendanceType.ATTENDANCE);
            scheduleAttendanceResultRepository.save(scheduleAttendanceResult);
        }else throw new Exception(" 출석번호가 일치하지 않습니다. 다시 시도해 주세요");

    }

    public void modifyMemberAttendance(Long scheduleId, Long memberId, String attendanceType){

        ScheduleAttendanceResult attendanceResult = scheduleAttendanceResultRepository.findByScheduleIdAndClubMemberId(scheduleId, memberId)
                .orElseThrow(() -> new RuntimeException("해당 일정에 대한 출석 결과가 존재하지 않습니다."));
        AttendanceType type = AttendanceType.valueOf(attendanceType.toUpperCase());

        if(type.equals(AttendanceType.OFFICIAL_ABSENCE)) {
            OfficialAbsenceRequest officialAbsenceRequest = OfficialAbsenceRequest.builder()
                    .clubMember(attendanceResult.getClubMember())
                    .schedule(attendanceResult.getSchedule())
                    .officialAbsenceContent("관리자의 출석 정정에 의해 인정된 공결입니다.")
                    .officialAbsenceRequestType(OfficialAbsenceRequestType.ACCEPTED)
                    .build();
            officialAbsenceRequestRepository.save(officialAbsenceRequest);
        }
        attendanceResult.setAttendanceType(type);
        scheduleAttendanceResultRepository.save(attendanceResult);
    }
}