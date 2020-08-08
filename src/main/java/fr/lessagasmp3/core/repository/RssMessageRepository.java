package fr.lessagasmp3.core.repository;

import fr.lessagasmp3.core.model.RssMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface RssMessageRepository extends JpaRepository<RssMessage, Long> {

    Set<RssMessage> findAllByFeedTitle(String feedTitle);

    List<RssMessage> findAllByPubdateAndTitleAndAuthor(String pubdate, String title, String author);
}
