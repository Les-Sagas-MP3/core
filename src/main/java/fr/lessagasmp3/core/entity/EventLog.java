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
}
