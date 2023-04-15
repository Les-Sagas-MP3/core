package fr.lessagasmp3.core.season.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lessagasmp3.core.episode.entity.Episode;
import fr.lessagasmp3.core.saga.entity.Saga;
import fr.lessagasmp3.core.season.model.SeasonModel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class Season extends SeasonModel {

    @ManyToOne(fetch = FetchType.LAZY)
    private Saga saga = new Saga();

    @OneToMany(mappedBy = "season", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Episode> episodes = new LinkedHashSet<>();

    public static Season fromModel(SeasonModel model) {
        Season entity = new Season();
        entity.setCreatedAt(model.getCreatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setId(model.getId());
        entity.setNumber(model.getNumber());
        entity.setName(model.getName());
        entity.setSagaRef(model.getSagaRef());
        entity.setEpisodesRef(model.getEpisodesRef());
        return entity;
    }
}
