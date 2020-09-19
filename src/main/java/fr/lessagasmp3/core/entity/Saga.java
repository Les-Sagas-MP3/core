package fr.lessagasmp3.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lessagasmp3.core.model.SagaModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@ToString
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
}
