package fr.lessagasmp3.core.file.model;

import fr.lessagasmp3.core.common.model.AuditModel;
import fr.lessagasmp3.core.file.entity.File;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
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

    @NotNull
    protected String format = "";

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
        model.setFormat(entity.getFormat());
        model.setContent(entity.getContent());
        model.setUrl(entity.getUrl());
        return model;
    }

    public String getFullname() {
        return name + "." + format;
    }

}
