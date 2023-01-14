package fr.lessagasmp3.core.episode.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lessagasmp3.core.episode.model.EpisodeModel;
import fr.lessagasmp3.core.file.entity.File;
import fr.lessagasmp3.core.season.entity.Season;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class Episode extends EpisodeModel {

    @ManyToOne
    @JsonIgnoreProperties(value = {"sagas", "episodes"})
    private Season season = new Season();

    @OneToOne
    private File file = null;

    public static Episode fromModel(EpisodeModel model) {
        Episode entity = new Episode();
        entity.setCreatedAt(model.getCreatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setId(model.getId());
        entity.setNumber(model.getNumber());
        entity.setDisplayedNumber(model.getDisplayedNumber());
        entity.setTitle(model.getTitle());
        entity.setInfos(model.getInfos());
        entity.setWorkspace(model.getWorkspace());
        entity.setSeasonRef(model.getSeasonRef());
        entity.setFileRef(model.getFileRef());
        return entity;
    }
}
