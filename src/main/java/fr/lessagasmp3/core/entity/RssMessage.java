package fr.lessagasmp3.core.entity;

import fr.lessagasmp3.core.model.RssMessageModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class RssMessage extends RssMessageModel {

    public static RssMessage fromModel(RssMessageModel model) {
        RssMessage entity = new RssMessage();
        entity.setCreatedAt(model.getCreatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setId(model.getId());
        entity.setFeedTitle(model.getFeedTitle());
        entity.setTitle(model.getTitle());
        entity.setPubdate(model.getPubdate());
        entity.setDescription(model.getDescription());
        entity.setLink(model.getLink());
        entity.setAuthor(model.getAuthor());
        entity.setGuid(model.getGuid());
        return entity;
    }
}
