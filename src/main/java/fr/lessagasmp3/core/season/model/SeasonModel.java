package fr.lessagasmp3.core.season.model;

import fr.lessagasmp3.core.common.model.AuditModel;
import fr.lessagasmp3.core.season.entity.Season;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@MappedSuperclass
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class SeasonModel extends AuditModel<String> implements Comparable<SeasonModel> {

    @NotNull
    protected Integer number;

    @NotNull
    protected String name;

    @Transient
    private Long sagaRef = 0L;

    @Transient
    private Set<Long> episodesRef = new LinkedHashSet<>();

    public static SeasonModel fromEntity(Season entity) {
        Objects.requireNonNull(entity);
        SeasonModel model = new SeasonModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setNumber(entity.getNumber());
        model.setName(entity.getName());
        if(entity.getSaga() != null) {
            model.setSagaRef(entity.getSaga().getId());
        }
        entity.getEpisodes().forEach(episode -> model.getEpisodesRef().add(episode.getId()));
        return model;
    }

    @Override
    public int compareTo(SeasonModel s) {
        return this.getNumber().compareTo(s.getNumber());
    }
}
