package fr.lessagasmp3.core.creator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lessagasmp3.core.creator.model.CreatorModel;
import fr.lessagasmp3.core.distribution.entity.DistributionEntry;
import fr.lessagasmp3.core.saga.entity.Saga;
import fr.lessagasmp3.core.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class Creator extends CreatorModel {

    @ManyToMany(mappedBy="authors", fetch = FetchType.LAZY)
    private Set<Saga> sagasWritten = new LinkedHashSet<>();

    @ManyToMany(mappedBy="composers", fetch = FetchType.LAZY)
    private Set<Saga> sagasComposed = new LinkedHashSet<>();

    @OneToMany(mappedBy = "actor", orphanRemoval = true)
    private Set<DistributionEntry> distributionEntries = new LinkedHashSet<>();

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user = null;

    public static Creator fromModel(CreatorModel model) {
        Creator entity = new Creator();
        entity.setCreatedAt(model.getCreatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setNbSagas(model.getNbSagas());
        entity.setUserRef(model.getUserRef());
        entity.setSagasWrittenRef(model.getSagasWrittenRef());
        entity.setSagasComposedRef(model.getSagasComposedRef());
        entity.setDistributionEntriesRef(model.getDistributionEntriesRef());
        return entity;
    }
}
