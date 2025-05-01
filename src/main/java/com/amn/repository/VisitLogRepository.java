package com.amn.repository;

import com.amn.entity.VisitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {
    List<VisitLog> findByMedicalFolder_Id(Long folderId);  // <-- add underscore _
}
