package fr.lessagasmp3.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lessagasmp3.core.model.SeasonModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@ToString
public class Season extends SeasonModel {

    @ManyToOne
    @JsonIgnoreProperties(value = {"authors", "composers", "categories", "seasons", "distributionEntries", "anecdotes"})
    private Saga saga = new Saga();

    @OneToMany(
            mappedBy = "season",
            orphanRemoval = true)
    @JsonIgnoreProperties(value = {"season"})
    private Set<Episode> episodes = new LinkedHashSet<>();

}
