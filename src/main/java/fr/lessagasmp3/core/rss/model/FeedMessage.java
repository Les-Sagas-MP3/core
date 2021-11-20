package fr.lessagasmp3.core.rss.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@ToString
public class FeedMessage {

    private String title;
    private String description;
    private String link;
    private String author;
    private String guid;

}
