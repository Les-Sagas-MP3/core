package fr.lessagasmp3.core.user.entity;

import fr.lessagasmp3.core.creator.entity.Creator;
import fr.lessagasmp3.core.user.model.UserModel;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity(name = "users")
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class User extends UserModel {

    @OneToOne(mappedBy = "user")
    private Creator creator;

    public User() {
        super();
    }

    public User(String email, String password) {
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static User fromModel(UserModel model) {
        User entity = new User();
        entity.setCreatedAt(model.getCreatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setId(model.getId());
        entity.setUsername(model.getUsername());
        entity.setPassword(model.getPassword());
        entity.setEmail(model.getEmail());
        entity.setEnabled(model.getEnabled());
        entity.setLastPasswordResetDate(model.getLastPasswordResetDate());
        entity.setCreatorRef(model.getCreatorRef());
        return entity;
    }
}
