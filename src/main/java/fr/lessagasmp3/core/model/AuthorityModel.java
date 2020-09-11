package fr.lessagasmp3.core.model;

import com.google.firebase.database.annotations.NotNull;
import fr.lessagasmp3.core.constant.AuthorityName;
import fr.lessagasmp3.core.entity.Authority;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@ToString
public class AuthorityModel extends AuditModel<String> {

    @Column(length = 50)
    @NotNull
    @Enumerated(EnumType.STRING)
    protected AuthorityName name;

    public static AuthorityModel fromEntity(Authority entity) {
        AuthorityModel model = new AuthorityModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setName(entity.getName());
        return model;
    }
}
