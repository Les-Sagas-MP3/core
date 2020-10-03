package fr.lessagasmp3.core.model;

import fr.lessagasmp3.core.entity.Season;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@MappedSuperclass
@ToString
public class SeasonModel extends AuditModel<String> {

    @NotNull
    protected Integer number;

    @Transient
    private Long sagaRef = 0L;

    @Transient
    private Set<Long> episodesRef = new LinkedHashSet<>();

    public static SeasonModel fromEntity(Season entity) {
        SeasonModel model = new SeasonModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setNumber(entity.getNumber());
        model.setSagaRef(entity.getSaga().getId());
        entity.getEpisodes().forEach(episode -> model.getEpisodesRef().add(episode.getId()));
        return model;
    }

}
