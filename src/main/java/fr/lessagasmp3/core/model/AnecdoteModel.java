package fr.lessagasmp3.core.model;

import fr.lessagasmp3.core.constant.Strings;
import fr.lessagasmp3.core.entity.Anecdote;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@MappedSuperclass
@ToString
public class AnecdoteModel extends AuditModel<String> {

    @Column(columnDefinition = "TEXT")
    protected String anecdote = Strings.EMPTY;

    public static AnecdoteModel fromEntity(Anecdote entity) {
        AnecdoteModel model = new AnecdoteModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setAnecdote(entity.getAnecdote());
        return model;
    }

}