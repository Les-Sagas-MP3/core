package fr.lessagasmp3.core.anecdote.model;

import fr.lessagasmp3.core.anecdote.entity.Anecdote;
import fr.lessagasmp3.core.common.constant.Strings;
import fr.lessagasmp3.core.common.model.AuditModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import java.util.Objects;

@MappedSuperclass
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class AnecdoteModel extends AuditModel<String> {

    @Column(columnDefinition = "TEXT")
    protected String anecdote = Strings.EMPTY;

    @Transient
    protected Long sagaRef = 0L;

    public static AnecdoteModel fromEntity(Anecdote entity) {
        Objects.requireNonNull(entity);
        AnecdoteModel model = new AnecdoteModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setAnecdote(entity.getAnecdote());
        if(entity.getSaga() != null) {
            model.setSagaRef(entity.getSaga().getId());
        }
        return model;
    }

}
