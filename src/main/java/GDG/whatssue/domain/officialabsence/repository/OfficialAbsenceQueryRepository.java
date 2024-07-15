package GDG.whatssue.domain.officialabsence.repository;

import GDG.whatssue.domain.officialabsence.dto.OfficialAbsenceGetRequestDto;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequestType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OfficialAbsenceQueryRepository {
    Page<OfficialAbsenceGetRequestDto> findOfficialAbsenceRequests(Long userId, Long clubId, OfficialAbsenceRequestType requestType, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
