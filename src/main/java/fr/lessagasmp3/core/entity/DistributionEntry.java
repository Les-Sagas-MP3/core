package fr.lessagasmp3.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lessagasmp3.core.model.DistributionEntryModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class DistributionEntry extends DistributionEntryModel {

    @ManyToOne
    @JsonIgnoreProperties(value = {"authors", "composers", "categories", "seasons", "distributionEntries", "anecdotes"})
    private Saga saga = new Saga();

    @ManyToOne
    @JsonIgnoreProperties(value = {"sagas", "user", "distributionEntries"})
    private Author actor = new Author();

    public static DistributionEntry fromModel(DistributionEntryModel model) {
        DistributionEntry entity = new DistributionEntry();
        entity.setCreatedAt(model.getCreatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setId(model.getId());
        entity.setRoles(model.getRoles());
        entity.setCreatorRef(model.getCreatorRef());
        entity.setSagaRef(model.getSagaRef());
        return entity;
    }
}
