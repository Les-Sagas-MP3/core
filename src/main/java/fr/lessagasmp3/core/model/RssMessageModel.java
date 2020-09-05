package fr.lessagasmp3.core.model;

import fr.lessagasmp3.core.constant.Strings;
import fr.lessagasmp3.core.entity.RssMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@MappedSuperclass
public class RssMessageModel extends AuditModel<String> {

    @NotNull
    protected String feedTitle = Strings.EMPTY;

    @NotNull
    protected String title = Strings.EMPTY;

    @NotNull
    protected Date pubdate = new Date();

    @Column(columnDefinition = "TEXT")
    @NotNull
    protected String description = Strings.EMPTY;

    @NotNull
    protected String link = Strings.EMPTY;

    @NotNull
    protected String author = Strings.EMPTY;

    @NotNull
    protected String guid = Strings.EMPTY;

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
