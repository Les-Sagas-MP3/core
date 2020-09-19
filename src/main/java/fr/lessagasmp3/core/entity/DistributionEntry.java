package fr.lessagasmp3.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lessagasmp3.core.model.DistributionEntryModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@ToString
public class DistributionEntry extends DistributionEntryModel {

    @ManyToOne
    @JsonIgnoreProperties(value = {"authors", "composers", "categories", "seasons", "distributionEntries", "anecdotes"})
    private Saga saga = new Saga();

    @ManyToOne
    @JsonIgnoreProperties(value = {"sagas", "user", "distributionEntries"})
    private Author actor = new Author();

}
