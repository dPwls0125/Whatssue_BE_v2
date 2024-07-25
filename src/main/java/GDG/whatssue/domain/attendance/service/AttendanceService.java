package GDG.whatssue.domain.attendance.service;

import GDG.whatssue.domain.attendance.Error.AttendanceErrorCode;
import GDG.whatssue.domain.attendance.dto.*;
import GDG.whatssue.domain.attendance.entity.AttendanceNum;
import GDG.whatssue.domain.attendance.repository.AttendanceNumRepository;
import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.entity.Role;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
import GDG.whatssue.domain.member.service.ClubMemberService;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static GDG.whatssue.domain.attendance.entity.AttendanceType.*;
import static GDG.whatssue.domain.member.entity.Role.MANAGER;
import static GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequestType.ACCEPTED;
import static GDG.whatssue.domain.schedule.entity.AttendanceStatus.BEFORE;
import static GDG.whatssue.domain.schedule.entity.AttendanceStatus.ONGOING;

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

        Schedule schedule = scheduleFacade.getScheduleById(scheduleId);
        // 출석 가능 여부 확인 및 예외 처리
        schedule.startAttendance();
        // 출석을 진행하기 전, 모든 멤버의 해당 일정의 출석 상태를 absence 으로 변경
        initializeMemberAttendance(clubId,scheduleId);
        // 출석번호 생성 및 맵에 저장
        int randomInt = putAttendanceNumInMapAndReturn(clubId, scheduleId);

        return AttendanceNumResponseDto.of(clubId, scheduleId, randomInt);

    }

    public MyAttendanceResultResponse getFilteredMemberAttendance(Long userId, Long clubId, LocalDate startDate, LocalDate endDate, String attendanceType, int size, int page) {

        Long memberId = getClubMemberId(clubId, userId);
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        attendanceType = attendanceType.toUpperCase();

        Page<ScheduleAttendanceResult> entityList;

        Pageable pageable = PageRequest.of(page,size);

        if(attendanceType.equals("TOTAL") ){

            entityList = scheduleAttendanceResultRepository.findAllByScheduleDateBetween(startDateTime, endDateTime, memberId, pageable);

        } else if (attendanceType.equals("ATTENDANCE") || attendanceType.equals("ABSENCE") || attendanceType.equals("OFFICIAL_ABSENCE")) {

            entityList = scheduleAttendanceResultRepository
                    .findAllByScheduleDateBetweenAndAttendanceType(startDateTime, endDateTime,
                            AttendanceType.valueOf(attendanceType) ,memberId ,pageable );

        }else{
            throw new CommonException(AttendanceErrorCode.EX5207);
        }

        List<ScheduleAttendanceResultDto> dtos =  entityList.stream()
                .map(ScheduleAttendanceResultDto::of)
                .collect(Collectors.toList());

        return MyAttendanceResultResponse.builder()
                .attendanceList(new PageImpl<>(dtos,pageable,entityList.getTotalElements()))
                .memberName(getClubMember(clubId, userId).getMemberName())
                .memberId(memberId)
                .build();
    }

    //현재 진행중인 일정 리스트
    public List<ScheduleDto> currentAttendanceList(Long clubId) {

        List<Schedule> scheduleList =  scheduleFacade.getSchedule(clubId, ONGOING);

        return scheduleList.stream()
                .filter(schedule -> schedule.getAttendanceStatus() == ONGOING)
                .filter(schedule -> isScheduleInMap(clubId, schedule.getId()))
                .map(ScheduleDto::of)
                .collect(Collectors.toList());
    }

    /*Delete 시에 결석자 명단을 업로드해야할까?*/
    @Transactional
    public void finishAttendanceOngoing(Long clubId, Long scheduleId) {

        Schedule schedule = scheduleFacade.getScheduleById(scheduleId);

        // 조건 체크 및 스케줄 상태 변경
        schedule.finishAttendance();

        // Map에서 출석 번호 삭제
        attendanceNumRepository.deleteById(getId(clubId,scheduleId));

    }

    @Transactional
    public void initAttendance(Long clubId, Long scheduleId) {

        Schedule schedule = scheduleFacade.getScheduleById(scheduleId);
        AttendanceStatus scheduleStatus = schedule.getAttendanceStatus();

        if(scheduleStatus == ONGOING) {
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

        } else throw new CommonException(AttendanceErrorCode.EX5204);
    }

    @Transactional
    public void modifyMemberAttendance(Long clubId, Long scheduleId, List<AttendmodifyDto> request){

        Schedule schedule = scheduleFacade.getScheduleById(scheduleId);

        if(schedule.getClub().getId() != clubId){
            throw new CommonException(ClubErrorCode.EX3206);
        }


        if(schedule.getAttendanceStatus() == ONGOING || schedule.getAttendanceStatus() == BEFORE){
            throw new CommonException(AttendanceErrorCode.Ex5209);
        }

        HashMap<AttendanceType,List<Long>> modifiedMemberMap = new HashMap<>(){
            {
                put(ATTENDANCE, new ArrayList<>());
                put(ABSENCE, new ArrayList<>());
                put(OFFICIAL_ABSENCE, new ArrayList<>());
            }
        };

        request.stream()
                .filter(dto -> isModified(dto))
                .filter(dto -> isMemberInClub(dto.getMemberId(), clubId))
                .forEach(dto -> {
                    modifiedMemberMap.get(dto.getAttendanceType()).add(dto.getMemberId());
                    isOfficialAbsence(getAttendanceResult(scheduleId, dto.getMemberId()),dto);
                });

        modifiedMemberMap.forEach((attendanceType, memberIdList) -> {
            if(!memberIdList.isEmpty()){
                scheduleAttendanceResultRepository.updateAttendanceTypeByScheduleIdAndClubMemberId(scheduleId, memberIdList, attendanceType);
            }
        });

    }

    public AttendanceTimesResponse getAttendanceTimes(Long clubId, Long memberId){

        ClubMember clubMember = getClubMember(memberId);
        Long memberClubId = clubMember.getClub().getId();

        if(memberClubId != clubId)
            throw new CommonException(ClubMemberErrorCode.EX2203);

        if(clubMember.getRole() == MANAGER) throw new CommonException(AttendanceErrorCode.Ex5210);

        return scheduleAttendanceResultRepository.countByClubIdIdAndAttendanceType(memberId);

    }

    private void initializeMemberAttendance(Long clubId, Long scheduleId) throws RuntimeException {

        List<ClubMember> clubMembers = clubMemberRepository.findByClubId(clubId).orElseThrow(()->new CommonException(ClubErrorCode.EX3100));

        for(ClubMember clubMember : clubMembers){

            if(scheduleAttendanceResultRepository.findByScheduleIdAndClubMemberId(scheduleId, clubMember.getId()).isPresent()){
                break;
            }

            if(isOfficial_Accepted(clubMember.getId(), scheduleId)){

                ScheduleAttendanceResult scheduleAttendanceResult = ScheduleAttendanceResult.builder()
                        .clubMember(clubMember)
                        .schedule(scheduleFacade.getScheduleById(scheduleId))
                        .attendanceType(OFFICIAL_ABSENCE)
                        .build();

                scheduleAttendanceResultRepository.save(scheduleAttendanceResult);

            }else{

                ScheduleAttendanceResult scheduleAttendanceResult = ScheduleAttendanceResult.builder()
                        .clubMember(clubMember)
                        .schedule(scheduleFacade.getScheduleById(scheduleId))
                        .attendanceType(AttendanceType.ABSENCE)
                        .build();
                scheduleAttendanceResultRepository.save(scheduleAttendanceResult);

            }
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
        return clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId)
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100))
                .getId();

    }

    private ClubMember getClubMember(Long clubId, Long userId){
        return clubMemberRepository.findById(getClubMemberId(clubId, userId))
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100));
    }

    private ClubMember getClubMember(Long memberId){
        return clubMemberRepository.findById(memberId).orElseThrow(() -> new CommonException(ClubMemberErrorCode.EX2100));
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

    private boolean isOfficial_Accepted(Long clubMemberId, Long scheduleId){
        return officialAbsenceRequestRepository.findByScheduleIdAndClubMemberId(scheduleId, clubMemberId)
                .map(officialAbsenceRequest -> officialAbsenceRequest.getOfficialAbsenceRequestType() == ACCEPTED)
                .orElse(false);
    }

    private ScheduleAttendanceResult getAttendanceResult(Long scheduleId, Long clubMemberId) {
        return scheduleAttendanceResultRepository.findByScheduleIdAndClubMemberId(scheduleId, clubMemberId)
                .orElseThrow(() -> new CommonException(AttendanceErrorCode.EX5201));
    }

    private boolean isModified(AttendmodifyDto dto) {
        if(dto.getIsModified()) return true;
        return false;
    }

    private void isOfficialAbsence(ScheduleAttendanceResult entity, AttendmodifyDto dto){
        if(dto.getAttendanceType().equals(OFFICIAL_ABSENCE)){

            if(officialAbsenceRequestRepository.findByClubMemberId(dto.getMemberId()).isPresent()){

                OfficialAbsenceRequest officialAbsenceRequest = officialAbsenceRequestRepository.findByClubMemberId(dto.getMemberId()).get();
                officialAbsenceRequest.setOfficialAbsenceRequestType(ACCEPTED);
                officialAbsenceRequest.setOfficialAbsenceContent("관리자의 출석 정정에 의해 인정된 공결입니다.");
                return;
            }

            OfficialAbsenceRequest officialAbsenceRequest = OfficialAbsenceRequest.builder()
                    .clubMember(entity.getClubMember())
                    .schedule(entity.getSchedule())
                    .officialAbsenceContent("관리자의 출석 정정에 의해 인정된 공결입니다.")
                    .officialAbsenceRequestType(ACCEPTED)
                    .build();

            officialAbsenceRequestRepository.save(officialAbsenceRequest);
        }
    }


    private boolean isMemberInClub(Long memberId, Long clubId){
        ClubMember member = getClubMember(memberId);
        return member.getClub().getId() == clubId; // 클럽에 속해있는지 확인
    }


}

