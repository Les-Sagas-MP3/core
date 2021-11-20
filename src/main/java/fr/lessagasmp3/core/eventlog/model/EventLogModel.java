package fr.lessagasmp3.core.eventlog.model;

import fr.lessagasmp3.core.common.constant.EventLogName;
import fr.lessagasmp3.core.eventlog.entity.EventLog;
import fr.lessagasmp3.core.common.model.AuditModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@MappedSuperclass
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class EventLogModel extends AuditModel<String> {

    @Column(length = 50)
    @NotNull
    @Enumerated(EnumType.STRING)
    protected EventLogName name;

    public static EventLogModel fromEntity(EventLog entity) {
        Objects.requireNonNull(entity);
        EventLogModel model = new EventLogModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setName(entity.getName());
        return model;
    }
}
