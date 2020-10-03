package fr.lessagasmp3.core.model;

import fr.lessagasmp3.core.entity.Episode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@MappedSuperclass
@ToString
public class EpisodeModel extends AuditModel<String> {

    @NotNull
    protected Integer number;

    @NotNull
    protected String title;

    @NotNull
    protected String infos;

    @Transient
    protected Long seasonRef = 0L;

    public static EpisodeModel fromEntity(Episode entity) {
        EpisodeModel model = new EpisodeModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setNumber(entity.getNumber());
        model.setTitle(entity.getTitle());
        model.setInfos(entity.getInfos());
        model.setSeasonRef(entity.getSeason().getId());
        return model;
    }

}
