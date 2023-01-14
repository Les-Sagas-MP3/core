package fr.lessagasmp3.core.file.entity;

import fr.lessagasmp3.core.file.model.FileModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class File extends FileModel {

    public static File fromModel(FileModel model) {
        File entity = new File();
        entity.setCreatedAt(model.getCreatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setId(model.getId());
        entity.setDirectory(model.getDirectory());
        entity.setName(model.getName());
        entity.setFormat(model.getFormat());
        entity.setContent(model.getContent());
        return entity;
    }
}
