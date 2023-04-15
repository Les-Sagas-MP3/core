package fr.lessagasmp3.core.distribution.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lessagasmp3.core.creator.entity.Creator;
import fr.lessagasmp3.core.distribution.model.DistributionEntryModel;
import fr.lessagasmp3.core.saga.entity.Saga;
import jakarta.persistence.FetchType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class DistributionEntry extends DistributionEntryModel {

    @ManyToOne(fetch = FetchType.LAZY)
    private Saga saga = new Saga();

    @ManyToOne(fetch = FetchType.LAZY)
    private Creator actor = new Creator();

    public static DistributionEntry fromModel(DistributionEntryModel model) {
        DistributionEntry entity = new DistributionEntry();
        entity.setCreatedAt(model.getCreatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setId(model.getId());
        entity.setRoles(model.getRoles());
        entity.setActorRef(model.getActorRef());
        entity.setSagaRef(model.getSagaRef());
        return entity;
    }
}
