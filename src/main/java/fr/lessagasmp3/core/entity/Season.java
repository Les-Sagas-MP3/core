package fr.lessagasmp3.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lessagasmp3.core.model.SeasonModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class Season extends SeasonModel {

    @ManyToOne
    @JsonIgnoreProperties(value = {"authors", "composers", "categories", "seasons", "distributionEntries", "anecdotes"})
    private Saga saga = new Saga();

    @OneToMany(
            mappedBy = "season",
            orphanRemoval = true)
    @JsonIgnoreProperties(value = {"season"})
    private Set<Episode> episodes = new LinkedHashSet<>();

    public static Season fromModel(SeasonModel model) {
        Season entity = new Season();
        entity.setCreatedAt(model.getCreatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setId(model.getId());
        entity.setNumber(model.getNumber());
        entity.setSagaRef(model.getSagaRef());
        entity.setEpisodesRef(model.getEpisodesRef());
        return entity;
    }
}
