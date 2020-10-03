package fr.lessagasmp3.core.model;

import fr.lessagasmp3.core.constant.Strings;
import fr.lessagasmp3.core.entity.DistributionEntry;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@MappedSuperclass
@ToString
public class DistributionEntryModel extends AuditModel<String> {

    @NotNull
    protected String roles = Strings.EMPTY;

    @Transient
    protected Long creatorRef = 0L;

    @Transient
    protected Long sagaRef = 0L;

    public static DistributionEntryModel fromEntity(DistributionEntry entity) {
        DistributionEntryModel model = new DistributionEntryModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setRoles(entity.getRoles());
        model.setCreatorRef(entity.getActor().getId());
        model.setSagaRef(entity.getSaga().getId());
        return model;
    }

}
