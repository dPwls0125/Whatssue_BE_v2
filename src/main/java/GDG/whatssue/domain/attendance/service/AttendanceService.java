package GDG.whatssue.domain.attendance.service;

import GDG.whatssue.domain.attendance.Error.AttendanceErrorCode;
import GDG.whatssue.domain.attendance.dto.AttendanceNumRequestDto;
import GDG.whatssue.domain.attendance.dto.AttendanceNumResponseDto;
import GDG.whatssue.domain.attendance.dto.ScheduleAttendanceMemberDto;
import GDG.whatssue.domain.attendance.dto.ScheduleDto;
import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequestType;
import GDG.whatssue.domain.officialabsence.repository.OfficialAbsenceRequestRepository;
import GDG.whatssue.domain.schedule.entity.AttendanceStatus;
import GDG.whatssue.domain.schedule.entity.Schedule;
import GDG.whatssue.domain.attendance.entity.AttendanceType;
import GDG.whatssue.domain.attendance.entity.ScheduleAttendanceResult;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.attendance.repository.ScheduleAttendanceResultRepository;
import GDG.whatssue.domain.schedule.repository.ScheduleRepository;
import java.time.LocalDateTime;

import GDG.whatssue.global.error.CommonException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {
    private static Map<Long, Map<Long, Integer>> attendanceNumMap = new HashMap<>();

    private final ScheduleAttendanceResultRepository scheduleAttendanceResultRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ScheduleRepository scheduleRepository;
    private final OfficialAbsenceRequestRepository officialAbsenceRequestRepository;
    public final static Random random = new Random();

    @Transactional
    public AttendanceNumResponseDto openAttendance(Long clubId, Long scheduleId) throws RuntimeException {

        Schedule schedule = scheduleRepository.findById(scheduleId).get();

        // 출석 가능 여부 확인 및 예외 처리
        schedule.checkIsAttendanced();
        // 출석을 진행하기 전, 모든 멤버의 해당 일정의 출석 상태를 absence 으로 변경
        initializeMemberAttendance(clubId);

        // 출석번호 생성 및 맵에 저장
        int randomInt = putAttendanceNumInMap(clubId, scheduleId);

        AttendanceNumResponseDto responseDto = AttendanceNumResponseDto.of(clubId, scheduleId, randomInt);

        return responseDto;
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
                        .scheduleDateTime(LocalDateTime.of(schedule.getScheduleDate(), schedule.getScheduleTime()))
                        .attendanceStatus(schedule.getAttendanceStatus())
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

    private void initializeMemberAttendance(Long clubId) throws RuntimeException {

        List<ClubMember> clubMembers = clubMemberRepository.findByClubId(clubId).orElseThrow(()->new CommonException(ClubErrorCode.NONE_CLUB_MEMBER_ERROR));

        for(ClubMember clubMember : clubMembers){

            ScheduleAttendanceResult scheduleAttendanceResult = ScheduleAttendanceResult.builder()
                    .clubMember(clubMember)
                    .attendanceType(AttendanceType.ABSENCE)
                    .build();

            scheduleAttendanceResultRepository.save(scheduleAttendanceResult);

        }
    }

    private int putAttendanceNumInMap(Long clubId, Long scheduleId){

        AttendanceService.attendanceNumMap.put(clubId, new HashMap<>());

        int randomInt = AttendanceService.random.nextInt(1, 1000);

        Map<Long, Integer> innerMap = attendanceNumMap.get(clubId);
        innerMap.put(scheduleId, randomInt);
        randomInt = attendanceNumMap.get(clubId).get(scheduleId);

        return randomInt;
    }
}