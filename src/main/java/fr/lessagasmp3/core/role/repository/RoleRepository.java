package fr.lessagasmp3.core.role.repository;

import fr.lessagasmp3.core.role.entity.Role;
import fr.lessagasmp3.core.role.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNameAndUserId(RoleName name, Long userRef);

    Optional<Role> findByNameAndUserIdAndSagaId(RoleName name, Long userRef, Long sagaRef);
}
