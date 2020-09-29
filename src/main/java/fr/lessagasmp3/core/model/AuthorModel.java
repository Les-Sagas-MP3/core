package fr.lessagasmp3.core.model;

import fr.lessagasmp3.core.entity.Author;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@MappedSuperclass
@ToString
public class AuthorModel extends AuditModel<String> {

    @NotNull
    protected String name = "";

    @NotNull
    protected Integer nbSagas = 0;

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
            return model;
        } else {
            return null;
        }
    }

}
