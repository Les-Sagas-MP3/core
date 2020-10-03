package fr.lessagasmp3.core.model;

import fr.lessagasmp3.core.entity.Author;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@MappedSuperclass
@ToString
public class AuthorModel extends AuditModel<String> {

    @NotNull
    protected String name = "";

    @NotNull
    protected Integer nbSagas = 0;

    @Transient
    private Set<Long> sagasRef = new LinkedHashSet<>();

    @Transient
    private Long userRef = 0L;

    public static AuthorModel fromEntity(Author entity) {
        if(entity != null) {
            AuthorModel model = new AuthorModel();
            model.setCreatedAt(entity.getCreatedAt());
            model.setCreatedBy(entity.getCreatedBy());
            model.setUpdatedAt(entity.getUpdatedAt());
            model.setUpdatedBy(entity.getUpdatedBy());
            model.setId(entity.getId());
            model.setName(entity.getName());
            model.setNbSagas(entity.getNbSagas());
            model.setUserRef(entity.getUser().getId());
            entity.getSagas().forEach(saga -> model.getSagasRef().add(saga.getId()));
            return model;
        } else {
            return null;
        }
    }

}
