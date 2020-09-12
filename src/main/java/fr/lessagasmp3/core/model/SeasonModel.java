package fr.lessagasmp3.core.model;

import fr.lessagasmp3.core.entity.Season;
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
public class SeasonModel extends AuditModel<String> {

    @NotNull
    protected Integer number;

    public static SeasonModel fromEntity(Season entity) {
        SeasonModel model = new SeasonModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setNumber(entity.getNumber());
        return model;
    }

}
