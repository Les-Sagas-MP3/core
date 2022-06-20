package fr.lessagasmp3.core.saga.model;

import fr.lessagasmp3.core.common.constant.SagaStatus;
import fr.lessagasmp3.core.common.constant.Strings;
import fr.lessagasmp3.core.common.model.AuditModel;
import fr.lessagasmp3.core.saga.entity.Saga;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@MappedSuperclass
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class SagaModel extends AuditModel<String> {

    @NotNull
    protected String title = Strings.EMPTY;

    @Column(length = 50)
    @NotNull
    @Enumerated(EnumType.STRING)
    protected SagaStatus status = SagaStatus.IN_PROGRESS;

    @NotNull
    protected Date startDate = new Date();

    @NotNull
    protected Long duration = 0L;

    @Column(columnDefinition = "TEXT")
    @NotNull
    protected String synopsis = Strings.EMPTY;

    @NotNull
    protected String origin = Strings.EMPTY;

    @Column(columnDefinition = "TEXT")
    @NotNull
    protected String genese = Strings.EMPTY;

    @Column(columnDefinition = "TEXT")
    @NotNull
    protected String awards = Strings.EMPTY;

    @NotNull
    protected String bannerUrl = Strings.EMPTY;

    @NotNull
    protected String coverUrl = Strings.EMPTY;

    @NotNull
    protected String url = Strings.EMPTY;

    @NotNull
    protected String urlWiki = Strings.EMPTY;

    @NotNull
    protected Integer levelArt = 100;

    @NotNull
    protected Integer levelTech = 100;

    @NotNull
    protected Integer nbReviews = 0;

    @NotNull
    protected String urlReviews = Strings.EMPTY;

    @NotNull
    protected Integer nbBravos = 0;

    @NotNull
    protected String workspace = Strings.EMPTY;

    @Transient
    private Set<Long> authorsRef = new LinkedHashSet<>();

    @Transient
    private Set<Long> composersRef = new LinkedHashSet<>();

    @Transient
    private Set<Long> categoriesRef = new LinkedHashSet<>();

    @Transient
    private Set<Long> seasonsRef = new LinkedHashSet<>();

    @Transient
    private Set<Long> distributionEntriesRef = new LinkedHashSet<>();

    @Transient
    private Set<Long> anecdotesRef = new LinkedHashSet<>();

    public static SagaModel fromEntity(Saga entity) {
        Objects.requireNonNull(entity);
        SagaModel model = new SagaModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        model.setStatus(entity.getStatus());
        model.setStartDate(entity.getStartDate());
        model.setDuration(entity.getDuration());
        model.setSynopsis(entity.getSynopsis());
        model.setOrigin(entity.getOrigin());
        model.setGenese(entity.getGenese());
        model.setAwards(entity.getAwards());
        model.setBannerUrl(entity.getBannerUrl());
        model.setCoverUrl(entity.getCoverUrl());
        model.setUrl(entity.getUrl());
        model.setUrlWiki(entity.getUrlWiki());
        model.setLevelArt(entity.getLevelArt());
        model.setLevelTech(entity.getLevelTech());
        model.setNbReviews(entity.getNbReviews());
        model.setUrlReviews(entity.getUrlReviews());
        model.setNbBravos(entity.getNbBravos());
        model.setWorkspace(entity.getWorkspace());
        entity.getAuthors().forEach(author -> model.getAuthorsRef().add(author.getId()));
        entity.getComposers().forEach(composer -> model.getComposersRef().add(composer.getId()));
        entity.getCategories().forEach(category -> model.getCategoriesRef().add(category.getId()));
        entity.getSeasons().forEach(season -> model.getSeasonsRef().add(season.getId()));
        entity.getDistributionEntries().forEach(distributionEntry -> model.getDistributionEntriesRef().add(distributionEntry.getId()));
        entity.getAnecdotes().forEach(anecdote -> model.getAnecdotesRef().add(anecdote.getId()));
        return model;
    }

}
