package fr.lessagasmp3.core.creator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lessagasmp3.core.creator.model.CreatorModel;
import fr.lessagasmp3.core.distribution.entity.DistributionEntry;
import fr.lessagasmp3.core.user.entity.User;
import fr.lessagasmp3.core.saga.entity.Saga;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class Creator extends CreatorModel {

    @ManyToMany(mappedBy="authors")
    @JsonIgnoreProperties(value = {"authors", "composers", "categories", "seasons", "distributionEntries", "anecdotes"})
    private Set<Saga> sagasWritten = new LinkedHashSet<>();

    @ManyToMany(mappedBy="composers")
    @JsonIgnoreProperties(value = {"authors", "composers", "categories", "seasons", "distributionEntries", "anecdotes"})
    private Set<Saga> sagasComposed = new LinkedHashSet<>();

    @OneToMany(
            mappedBy = "actor",
            orphanRemoval = true)
    @JsonIgnoreProperties(value = {"saga", "actors"})
    private Set<DistributionEntry> distributionEntries = new LinkedHashSet<>();

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = {"username", "password", "email", "enabled", "lastPasswordResetDate", "authorities"})
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
