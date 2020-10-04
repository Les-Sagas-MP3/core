package fr.lessagasmp3.core.entity;

import fr.lessagasmp3.core.constant.EventLogName;
import fr.lessagasmp3.core.model.EventLogModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@ToString
public class EventLog extends EventLogModel {

    public EventLog() {}

    public EventLog(EventLogName name) {
        super();
        this.name = name;
    }

    public static EventLog fromModel(EventLogModel model) {
        EventLog entity = new EventLog();
        entity.setCreatedAt(model.getCreatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setId(model.getId());
        entity.setName(model.getName());
        return entity;
    }
}
