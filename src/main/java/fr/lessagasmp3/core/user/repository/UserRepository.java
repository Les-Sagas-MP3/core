package fr.lessagasmp3.core.user.repository;

import fr.lessagasmp3.core.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String username);

    Set<User> findAllByUsernameContainsIgnoreCaseOrderByUsernameAsc(String username);
}
