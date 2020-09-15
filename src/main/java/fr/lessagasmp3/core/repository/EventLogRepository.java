package fr.lessagasmp3.core.repository;

import fr.lessagasmp3.core.constant.EventLogName;
import fr.lessagasmp3.core.entity.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventLogRepository extends JpaRepository<EventLog, Long> {

    EventLog findTopByNameOrderByCreatedAtDesc(EventLogName name);

}
