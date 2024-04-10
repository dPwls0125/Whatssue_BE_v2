package GDG.whatssue.domain.officialabsence.repository;

import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequestType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfficialAbsenceRequestRepository extends JpaRepository<OfficialAbsenceRequest, Long> {

    List<OfficialAbsenceRequest> findByOfficialAbsenceRequestType(OfficialAbsenceRequestType officialAbsenceRequestType);

    List<OfficialAbsenceRequest> findByOfficialAbsenceRequestTypeNot(OfficialAbsenceRequestType officialAbsenceRequestType);
}
