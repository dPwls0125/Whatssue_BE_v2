package GDG.whatssue.domain.officialabsence.repository;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.officialabsence.dto.OfficialAbsenceGetRequestDto;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequestType;
import GDG.whatssue.domain.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OfficialAbsenceRequestRepository extends JpaRepository<OfficialAbsenceRequest, Long>,OfficialAbsenceQueryRepository  {

    Page<OfficialAbsenceRequest> findByClubMember_Club_IdAndOfficialAbsenceRequestType(Long clubId, OfficialAbsenceRequestType officialAbsenceRequestType, Pageable pageable);

    Page<OfficialAbsenceRequest> findByClubMember_Club_IdAndOfficialAbsenceRequestTypeNot(Long clubId, OfficialAbsenceRequestType officialAbsenceRequestType, Pageable pageable);

    boolean existsByScheduleAndClubMember(Schedule schedule, ClubMember clubMember);

    Optional<OfficialAbsenceRequest> findByScheduleIdAndClubMemberId(Long scheduleId, Long clubMemberId);

    Page<OfficialAbsenceRequest> findByClubMember(ClubMember clubMember, Pageable pageable);
    Page<OfficialAbsenceRequest> findByClubMember_Club_Id(Long clubId, Pageable pageable);

    Optional<OfficialAbsenceRequest> findByClubMember_Club_IdAndId(Long clubId, Long officialAbsenceId);

    Page<OfficialAbsenceRequest> findByClubMember_Club_IdAndOfficialAbsenceRequestTypeNotAndSchedule_ScheduleDateBetween(Long clubId, OfficialAbsenceRequestType officialAbsenceRequestType, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Optional<OfficialAbsenceRequest> findByClubMemberId(Long clubMemberId);
}
