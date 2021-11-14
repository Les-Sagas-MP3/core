package fr.lessagasmp3.core.eventlog.controller;

import fr.lessagasmp3.core.constant.EventLogName;
import fr.lessagasmp3.core.entity.EventLog;
import fr.lessagasmp3.core.model.EventLogModel;
import fr.lessagasmp3.core.repository.EventLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EventLogController {

    @Autowired
    private EventLogRepository eventLogRepository;

    @RequestMapping(value = "/eventlogs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"name"})
    public EventLogModel getLatestByName(@RequestParam EventLogName name) {
        EventLog entity = eventLogRepository.findTopByNameOrderByCreatedAtDesc(name);
        if(entity != null) {
            return EventLogModel.fromEntity(entity);
        }
        return null;
    }

}
