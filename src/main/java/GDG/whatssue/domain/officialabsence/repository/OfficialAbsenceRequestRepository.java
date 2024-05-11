package GDG.whatssue.domain.officialabsence.repository;

import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequestType;
import GDG.whatssue.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfficialAbsenceRequestRepository extends JpaRepository<OfficialAbsenceRequest, Long> {

    List<OfficialAbsenceRequest> findByOfficialAbsenceRequestType(OfficialAbsenceRequestType officialAbsenceRequestType);

    List<OfficialAbsenceRequest> findByOfficialAbsenceRequestTypeNot(OfficialAbsenceRequestType officialAbsenceRequestType);

    boolean existsByScheduleAndClubMember(Schedule schedule, ClubMember clubMember);
}
