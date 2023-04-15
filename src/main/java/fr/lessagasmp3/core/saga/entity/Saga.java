package fr.lessagasmp3.core.saga.entity;

import fr.lessagasmp3.core.anecdote.entity.Anecdote;
import fr.lessagasmp3.core.category.entity.Category;
import fr.lessagasmp3.core.creator.entity.Creator;
import fr.lessagasmp3.core.distribution.entity.DistributionEntry;
import fr.lessagasmp3.core.saga.model.SagaModel;
import fr.lessagasmp3.core.season.entity.Season;
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
public class Saga extends SagaModel {

    @ManyToMany
    @JoinTable(
            name = "saga_authors",
            joinColumns = {@JoinColumn(name = "sagas_written_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authors_id", referencedColumnName = "id")})
    private Set<Creator> authors = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(
            name = "saga_composers",
            joinColumns = {@JoinColumn(name = "sagas_composed_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "composers_id", referencedColumnName = "id")})
    private Set<Creator> composers = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(
            name = "saga_categories",
            joinColumns = {@JoinColumn(name = "sagas_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "categories_id", referencedColumnName = "id")})
    private Set<Category> categories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "saga", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Season> seasons = new LinkedHashSet<>();

    @OneToMany(mappedBy = "saga", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<DistributionEntry> distributionEntries = new LinkedHashSet<>();

    @OneToMany(mappedBy = "saga", orphanRemoval = true, fetch = FetchType.LAZY)
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
        entity.setGenese(model.getGenese());
        entity.setAwards(model.getAwards());
        entity.setBannerUrl(model.getBannerUrl());
        entity.setCoverUrl(model.getCoverUrl());
        entity.setUrl(model.getUrl());
        entity.setUrlWiki(model.getUrlWiki());
        entity.setLevelArt(model.getLevelArt());
        entity.setLevelTech(model.getLevelTech());
        entity.setNbReviews(model.getNbReviews());
        entity.setUrlReviews(model.getUrlReviews());
        entity.setNbBravos(model.getNbBravos());
        entity.setWorkspace(model.getWorkspace());
        entity.setAuthorsRef(model.getAuthorsRef());
        entity.setComposersRef(model.getComposersRef());
        entity.setCategoriesRef(model.getCategoriesRef());
        entity.setSeasonsRef(model.getSeasonsRef());
        entity.setDistributionEntriesRef(model.getDistributionEntriesRef());
        entity.setAnecdotesRef(model.getAnecdotesRef());
        return entity;
    }
}
