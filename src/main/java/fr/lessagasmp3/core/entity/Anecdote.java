package fr.lessagasmp3.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lessagasmp3.core.model.AnecdoteModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@ToString
public class Anecdote extends AnecdoteModel {

    @ManyToOne
    @JsonIgnoreProperties(value = {"authors", "composers", "categories", "seasons", "distributionEntries", "anecdotes"})
    private Saga saga = new Saga();

}
