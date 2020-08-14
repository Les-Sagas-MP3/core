package fr.lessagasmp3.core.model;

import fr.lessagasmp3.core.entity.RssMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@MappedSuperclass
public class RssMessageModel extends AuditModel<String> {

    @NotNull
    protected String feedTitle;

    @NotNull
    protected String title;

    @NotNull
    protected String pubdate;

    @Column(columnDefinition = "TEXT")
    @NotNull
    protected String description;

    @NotNull
    protected String link;

    @NotNull
    protected String author;

    @NotNull
    protected String guid;

    public static RssMessageModel fromEntity(RssMessage entity) {
        RssMessageModel model = new RssMessageModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setFeedTitle(entity.getFeedTitle());
        model.setTitle(entity.getTitle());
        model.setPubdate(entity.getPubdate());
        model.setDescription(entity.getDescription());
        model.setLink(entity.getLink());
        model.setAuthor(entity.getAuthor());
        model.setGuid(entity.getGuid());
        return model;
    }

}
