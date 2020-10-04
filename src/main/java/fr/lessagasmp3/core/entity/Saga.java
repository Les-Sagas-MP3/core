package fr.lessagasmp3.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lessagasmp3.core.model.SagaModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class Saga extends SagaModel {

    @ManyToMany
    @JsonIgnoreProperties(value = {"sagas", "user"})
    private Set<Author> authors = new LinkedHashSet<>();

    @ManyToMany
    @JsonIgnoreProperties(value = {"sagas", "user"})
    private Set<Author> composers = new LinkedHashSet<>();

    @ManyToMany
    @JsonIgnoreProperties(value = {"sagas"})
    private Set<Category> categories = new LinkedHashSet<>();

    @OneToMany(
            mappedBy = "saga",
            orphanRemoval = true)
    @JsonIgnoreProperties(value = {"saga", "episodes"})
    private Set<Season> seasons = new LinkedHashSet<>();

    @OneToMany(
            mappedBy = "saga",
            orphanRemoval = true)
    @JsonIgnoreProperties(value = {"saga", "actors"})
    private Set<DistributionEntry> distributionEntries = new LinkedHashSet<>();

    @OneToMany(
            mappedBy = "saga",
            orphanRemoval = true)
    @JsonIgnoreProperties(value = {"saga"})
    private Set<Anecdote> anecdotes = new LinkedHashSet<>();

    public static Saga fromModel(SagaModel model) {
        Saga entity = new Saga();
        entity.setCreatedAt(model.getCreatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setId(model.getId());
        entity.setTitle(model.getTitle());
        entity.setStatus(model.getStatus());
        entity.setStartDate(model.getStartDate());
        entity.setDuration(model.getDuration());
        entity.setSynopsis(model.getSynopsis());
        entity.setOrigin(model.getOrigin());
        entity.setBackgroundUrl(model.getBackgroundUrl());
        entity.setCoverUrl(model.getCoverUrl());
        entity.setUrl(model.getUrl());
        entity.setUrlWiki(model.getUrlWiki());
        entity.setLevelArt(model.getLevelArt());
        entity.setLevelTech(model.getLevelTech());
        entity.setNbReviews(model.getNbReviews());
        entity.setUrlReviews(model.getUrlReviews());
        entity.setNbBravos(model.getNbBravos());
        entity.setAuthorsRef(model.getAuthorsRef());
        entity.setComposersRef(model.getComposersRef());
        entity.setCategoriesRef(model.getCategoriesRef());
        entity.setSeasonsRef(model.getSeasonsRef());
        entity.setDistributionEntriesRef(model.getDistributionEntriesRef());
        entity.setAnecdotesRef(model.getAnecdotesRef());
        return entity;
    }
}
