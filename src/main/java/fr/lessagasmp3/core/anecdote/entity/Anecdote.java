package fr.lessagasmp3.core.anecdote.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lessagasmp3.core.anecdote.model.AnecdoteModel;
import fr.lessagasmp3.core.saga.entity.Saga;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class Anecdote extends AnecdoteModel {

    @ManyToOne
    @JsonIgnoreProperties(value = {"authors", "composers", "categories", "seasons", "distributionEntries", "anecdotes"})
    private Saga saga = new Saga();

    public static Anecdote fromModel(AnecdoteModel model) {
        Anecdote entity = new Anecdote();
        entity.setCreatedAt(model.getCreatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setId(model.getId());
        entity.setAnecdote(model.getAnecdote());
        entity.setSagaRef(model.getSagaRef());
        return entity;
    }

}
