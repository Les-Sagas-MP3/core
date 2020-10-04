package fr.lessagasmp3.core.model;

import fr.lessagasmp3.core.entity.Creator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@MappedSuperclass
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class CreatorModel extends AuditModel<String> {

    @NotNull
    protected String name = "";

    @NotNull
    protected Integer nbSagas = 0;

    @Transient
    private Long userRef = 0L;

    @Transient
    private Set<Long> sagasWrittenRef = new LinkedHashSet<>();

    @Transient
    private Set<Long> sagasComposedRef = new LinkedHashSet<>();

    @Transient
    private Set<Long> distributionEntriesRef = new LinkedHashSet<>();

    public static CreatorModel fromEntity(Creator entity) {
        Objects.requireNonNull(entity);
        CreatorModel model = new CreatorModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setNbSagas(entity.getNbSagas());
        if (entity.getUser() != null) {
            model.setUserRef(entity.getUser().getId());
        }
        entity.getSagasWritten().forEach(saga -> model.getSagasWrittenRef().add(saga.getId()));
        entity.getSagasComposed().forEach(saga -> model.getSagasComposedRef().add(saga.getId()));
        entity.getDistributionEntries().forEach(distributionEntry -> model.getDistributionEntriesRef().add(distributionEntry.getId()));
        return model;
    }

}

