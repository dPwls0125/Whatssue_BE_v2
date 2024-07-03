package GDG.whatssue.domain.attendance.service;

import GDG.whatssue.domain.attendance.Error.AttendanceErrorCode;
import GDG.whatssue.domain.attendance.dto.*;
import GDG.whatssue.domain.attendance.entity.AttendanceNum;
import GDG.whatssue.domain.attendance.repository.AttendanceNumRepository;
import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.service.ClubMemberService;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequestType;
import GDG.whatssue.domain.officialabsence.repository.OfficialAbsenceRequestRepository;
import GDG.whatssue.domain.schedule.entity.AttendanceStatus;
import GDG.whatssue.domain.schedule.entity.Schedule;
import GDG.whatssue.domain.attendance.entity.AttendanceType;
import GDG.whatssue.domain.attendance.entity.ScheduleAttendanceResult;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.attendance.repository.ScheduleAttendanceResultRepository;
import GDG.whatssue.domain.schedule.service.ScheduleFacade;
import GDG.whatssue.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {


    private final ScheduleAttendanceResultRepository scheduleAttendanceResultRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ScheduleFacade scheduleFacade;
    private final AttendanceFacade attendanceFacade;
    private final AttendanceNumRepository attendanceNumRepository;
    private final OfficialAbsenceRequestRepository officialAbsenceRequestRepository;
    private final ClubMemberService clubMemberService;
    public final static Random random = new Random();

    @Transactional
    public AttendanceNumResponseDto openAttendance(Long clubId, Long scheduleId) {

        Schedule schedule = scheduleFacade.getSchedule(clubId, scheduleId);
        // 출석 가능 여부 확인 및 예외 처리
        schedule.startAttendance();
        // 출석을 진행하기 전, 모든 멤버의 해당 일정의 출석 상태를 absence 으로 변경
        initializeMemberAttendance(clubId,scheduleId);
        // 출석번호 생성 및 맵에 저장
        int randomInt = putAttendanceNumInMapAndReturn(clubId, scheduleId);

        AttendanceNumResponseDto responseDto = AttendanceNumResponseDto.of(clubId, scheduleId, randomInt);

        return responseDto;

    }

    public List<ScheduleAttendanceResultDto> getFilteredMemberAttendance(Long userId, Long clubId, LocalDate startDate, LocalDate endDate, String attendanceType) {

        Long memberId = getClubMemberId(clubId, userId);
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        attendanceType = attendanceType.toUpperCase();
        System.out.println(attendanceType);

        if(attendanceType.equals("TOTAL") ){
            List<ScheduleAttendanceResult> entityList = scheduleAttendanceResultRepository.findAllByScheduleDateBetween(startDateTime, endDateTime, memberId);
            return entityList.stream()
                    .map(ScheduleAttendanceResultDto::of)
                    .collect(Collectors.toList());
        } else if (attendanceType.equals("ATTENDANCE") || attendanceType.equals("ABSENCE") || attendanceType.equals("OFFICIAL_ABSENCE")) {
            List<ScheduleAttendanceResult> entityList = scheduleAttendanceResultRepository.findAllByScheduleDateBetweenAndAttendanceType(startDateTime, endDateTime, AttendanceType.valueOf(attendanceType), memberId);
            return entityList.stream()
                    .map(ScheduleAttendanceResultDto::of)
                    .collect(Collectors.toList());
        }else{
            throw new CommonException(AttendanceErrorCode.EX5207);
        }
    }

    //현재 진행중인 일정 리스트
    public List<ScheduleDto> currentAttendanceList(Long clubId) {

        List<Schedule> scheduleList =  scheduleFacade.getSchedule(clubId, AttendanceStatus.ONGOING);

        return scheduleList.stream()
                .filter(schedule -> schedule.getAttendanceStatus() == AttendanceStatus.ONGOING)
                .filter(schedule -> isScheduleInMap(clubId, schedule.getId()))
                .map(ScheduleDto::of)
                .collect(Collectors.toList());
    }

    /*Delete 시에 결석자 명단을 업로드해야할까?*/
    @Transactional
    public void finishAttendanceOngoing(Long clubId, Long scheduleId) {

        Schedule schedule = scheduleFacade.getSchedule(clubId, scheduleId);

        // 조건 체크 및 스케줄 상태 변경
        schedule.finishAttendance();

        // Map에서 출석 번호 삭제
        attendanceNumRepository.deleteById(getId(clubId,scheduleId));

    }

//    public getMemberAttendanceList(Long clubId, Long scheduleId, Long memberId) {
//
//    }


    @Transactional
    public void initAttendance(Long clubId, Long scheduleId) {
        Schedule schedule = scheduleFacade.getSchedule(clubId, scheduleId);
        AttendanceStatus scheduleStatus = schedule.getAttendanceStatus();

        if(scheduleStatus == AttendanceStatus.ONGOING) {
            isScheduleInMap(clubId, scheduleId);
            attendanceNumRepository.deleteById(getId(clubId,scheduleId));
        }

        scheduleAttendanceResultRepository.deleteByScheduleId(scheduleId);
        schedule.initAttendance();

    }

    public List<ScheduleAttendanceMemberDto> getAttendanceList(Long scheduleId) {
        List<ScheduleAttendanceResult> attendanceList = attendanceFacade.getAttendanceResultbySchedule(scheduleId);
        return ScheduleAttendanceMemberDto.of(attendanceList);
    }

    @Transactional
    public void doAttendance(Long clubId, Long scheduleId, Long userId, AttendanceNumRequestDto requestDto) {

        int inputValue = requestDto.getAttendanceNum();

        if (getStoredNum(clubId,scheduleId) == inputValue) {
            ScheduleAttendanceResult scheduleAttendanceResult = attendanceFacade.getAttendanceResult(scheduleId, getClubMemberId(clubId, userId));
            if(scheduleAttendanceResult.getAttendanceType() == AttendanceType.ATTENDANCE){
                throw new CommonException(AttendanceErrorCode.EX5205);
            }
            scheduleAttendanceResult.setAttendanceType(AttendanceType.ATTENDANCE);
        }else throw new CommonException(AttendanceErrorCode.EX5204);
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



    private void initializeMemberAttendance(Long clubId, Long scheduleId) throws RuntimeException {

        List<ClubMember> clubMembers = clubMemberRepository.findByClubId(clubId).orElseThrow(()->new CommonException(ClubErrorCode.EX3100));

        for(ClubMember clubMember : clubMembers){
            if(scheduleAttendanceResultRepository.findByScheduleIdAndClubMemberId(scheduleId, clubMember.getId()).isPresent()){
                break;
            }
            ScheduleAttendanceResult scheduleAttendanceResult = ScheduleAttendanceResult.builder()
                    .clubMember(clubMember)
                    .schedule(scheduleFacade.getSchedule(clubId, scheduleId))
                    .attendanceType(AttendanceType.ABSENCE)
                    .build();
            scheduleAttendanceResultRepository.save(scheduleAttendanceResult);
        }
    }

    private int putAttendanceNumInMapAndReturn(Long clubId, Long scheduleId){

        int randomInt = AttendanceService.random.nextInt(1, 1000);

        String id = "attendanceNum" + clubId.toString() +":" + scheduleId.toString();

        attendanceNumRepository.save(
                AttendanceNum.builder()
                .id(id)
                .certificationNum(randomInt)
                .build());

        AttendanceNum attendanceNum  = attendanceNumRepository.findById(id).orElseThrow( () -> new CommonException(AttendanceErrorCode.EX5206) );

        return attendanceNum.getCertificationNum();
    }

    private Long getClubMemberId(Long clubId, Long userId) {
        return clubMemberService.getClubMemberId(clubId, userId);
    }

    private int getStoredNum(Long clubId, Long scheduleId) {

        isScheduleInMap(clubId, scheduleId);
        String mapId = "attendanceNum" + clubId.toString() +":" + scheduleId.toString();
        return attendanceNumRepository.findById(mapId)
                .orElseThrow( () -> new CommonException(AttendanceErrorCode.EX5206) )
                .getCertificationNum();
    }

    private boolean isScheduleInMap(Long clubId, Long scheduleId) {

        if (!attendanceNumRepository.findById(getId(clubId,scheduleId)).isPresent()) {
            throw new CommonException(AttendanceErrorCode.EX5203);
        }
        return true;
    }

    private String getId(Long clubId, Long scheduleId) {
        return "attendanceNum" + clubId.toString() +":" + scheduleId.toString();
    }

}