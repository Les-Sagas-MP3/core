package fr.lessagasmp3.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lessagasmp3.core.model.EpisodeModel;
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
public class Episode extends EpisodeModel {

    @ManyToOne
    @JsonIgnoreProperties(value = {"sagas", "episodes"})
    private Season season = new Season();

    public static Episode fromModel(EpisodeModel model) {
        Episode entity = new Episode();
        entity.setCreatedAt(model.getCreatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setId(model.getId());
        entity.setNumber(model.getNumber());
        entity.setTitle(model.getTitle());
        entity.setInfos(model.getInfos());
        entity.setSeasonRef(model.getSeasonRef());
        return entity;
    }
}
