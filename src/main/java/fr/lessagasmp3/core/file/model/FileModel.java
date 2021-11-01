package fr.lessagasmp3.core.file.model;

import fr.lessagasmp3.core.file.entity.File;
import fr.lessagasmp3.core.model.AuditModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@MappedSuperclass
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class FileModel extends AuditModel<String> {

    @NotNull
    protected String directory = "";

    @NotNull
    protected String name = "";

    @Column(columnDefinition = "TEXT")
    @NotNull
    protected String content;

    @NotNull
    protected String url = "";

    public static FileModel fromEntity(File entity) {
        Objects.requireNonNull(entity);
        FileModel model = new FileModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setDirectory(entity.getDirectory());
        model.setName(entity.getName());
        model.setContent(entity.getContent());
        model.setUrl(entity.getUrl());
        return model;
    }

}
