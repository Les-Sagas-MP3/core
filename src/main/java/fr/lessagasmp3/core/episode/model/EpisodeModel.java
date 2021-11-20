package fr.lessagasmp3.core.episode.model;

import fr.lessagasmp3.core.episode.entity.Episode;
import fr.lessagasmp3.core.common.model.AuditModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@MappedSuperclass
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class EpisodeModel extends AuditModel<String> implements Comparable<EpisodeModel> {

    @NotNull
    protected Integer number;

    @NotNull
    protected String displayedNumber;

    @NotNull
    protected String title;

    @NotNull
    protected String infos;

    @Transient
    protected Long seasonRef = 0L;

    public static EpisodeModel fromEntity(Episode entity) {
        Objects.requireNonNull(entity);
        EpisodeModel model = new EpisodeModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setNumber(entity.getNumber());
        model.setDisplayedNumber(entity.getDisplayedNumber());
        model.setTitle(entity.getTitle());
        model.setInfos(entity.getInfos());
        if(entity.getSeason() != null) {
            model.setSeasonRef(entity.getSeason().getId());
        }
        return model;
    }

    @Override
    public int compareTo(EpisodeModel e) {
        return this.getNumber().compareTo(e.getNumber());
    }
}
