package fr.lessagasmp3.core.model;

import fr.lessagasmp3.core.constant.Strings;
import fr.lessagasmp3.core.entity.DistributionEntry;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@MappedSuperclass
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class DistributionEntryModel extends AuditModel<String> {

    @NotNull
    protected String roles = Strings.EMPTY;

    @Transient
    protected Long actorRef = 0L;

    @Transient
    protected Long sagaRef = 0L;

    public static DistributionEntryModel fromEntity(DistributionEntry entity) {
        Objects.requireNonNull(entity);
        DistributionEntryModel model = new DistributionEntryModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setRoles(entity.getRoles());
        if(entity.getActor() != null) {
            model.setActorRef(entity.getActor().getId());
        }
        if (entity.getSaga() != null) {
            model.setSagaRef(entity.getSaga().getId());
        }
        return model;
    }

}
