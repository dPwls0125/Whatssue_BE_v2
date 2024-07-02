package GDG.whatssue.domain.attendance.repository;

import GDG.whatssue.domain.attendance.entity.AttendanceNum;
import GDG.whatssue.domain.user.entity.PhoneCertNum;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AttendanceNumRepository extends CrudRepository<AttendanceNum, String> {
    Optional<AttendanceNum> findById(String id);
}

