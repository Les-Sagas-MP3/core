package fr.lessagasmp3.core.repository;

import fr.lessagasmp3.core.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
    File findByDirectoryAndName(String directory, String name);
}
