package fr.lessagasmp3.core.eventlog.repository;

import fr.lessagasmp3.core.common.constant.EventLogName;
import fr.lessagasmp3.core.eventlog.entity.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface EventLogRepository extends JpaRepository<EventLog, Long> {

    EventLog findTopByNameOrderByCreatedAtDesc(EventLogName name);

    List<EventLog> findAllByOrderByCreatedAtDesc();

    void deleteAllByNameAndCreatedAtBefore(EventLogName name, Date createdAt);
}
