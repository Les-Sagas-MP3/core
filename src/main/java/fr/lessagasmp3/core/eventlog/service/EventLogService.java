package fr.lessagasmp3.core.eventlog.service;

import fr.lessagasmp3.core.constant.EventLogName;
import fr.lessagasmp3.core.entity.EventLog;
import fr.lessagasmp3.core.repository.EventLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
public class EventLogService {

    @Autowired
    private EventLogRepository eventLogRepository;

    public EventLog insert(EventLog eventLog) {
        return eventLogRepository.save(eventLog);
    }

    @Transactional(propagation = REQUIRES_NEW)
    public void rotate(EventLogName name) {
        List<EventLog> eventLogs = eventLogRepository.findAllByOrderByCreatedAtDesc();
        if(eventLogs.size() > 10) {
            eventLogRepository.deleteAllByNameAndCreatedAtBefore(name, eventLogs.get(10).getCreatedAt());
        }
    }

}
