package fr.lessagasmp3.core.model;

import fr.lessagasmp3.core.constant.Strings;
import fr.lessagasmp3.core.entity.Saga;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@MappedSuperclass
public class SagaModel extends AuditModel<String> {

    @NotNull
    protected String title = Strings.EMPTY;

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

    @Transient
    private Set<Long> authorsRef = new LinkedHashSet<>();

    @Transient
    private Set<Long> categoriesRef = new LinkedHashSet<>();

    @Transient
    private Set<Long> seasonsRef = new LinkedHashSet<>();

    public static SagaModel fromEntity(Saga entity) {
        Objects.requireNonNull(entity);
        SagaModel model = new SagaModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        model.setUrl(entity.getUrl());
        model.setUrlWiki(entity.getUrlWiki());
        model.setLevelArt(entity.getLevelArt());
        model.setLevelTech(entity.getLevelTech());
        model.setNbReviews(entity.getNbReviews());
        model.setUrlReviews(entity.getUrlReviews());
        model.setNbBravos(entity.getNbBravos());
        entity.getAuthors().forEach(author -> model.getAuthorsRef().add(author.getId()));
        entity.getCategories().forEach(category -> model.getCategoriesRef().add(category.getId()));
        entity.getSeasons().forEach(season -> model.getSeasonsRef().add(season.getId()));
        return model;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SagaModel{");
        sb.append("id=").append(id).append('\'');
        sb.append(", title='").append(title);
        sb.append('}');
        return sb.toString();
    }
}
