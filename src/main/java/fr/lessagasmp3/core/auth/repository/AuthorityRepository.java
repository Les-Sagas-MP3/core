package fr.lessagasmp3.core.auth.repository;

import fr.lessagasmp3.core.auth.entity.Authority;
import fr.lessagasmp3.core.common.constant.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Set<Authority> findAllByUsers_Id(Long userId);

    Authority findByName(AuthorityName roleUser);
    
    Authority findByNameAndUsers_Id(AuthorityName role, Long userId);

}
