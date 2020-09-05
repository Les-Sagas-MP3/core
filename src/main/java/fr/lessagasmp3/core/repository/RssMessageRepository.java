package fr.lessagasmp3.core.repository;

import fr.lessagasmp3.core.entity.RssMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface RssMessageRepository extends JpaRepository<RssMessage, Long> {

    Set<RssMessage> findAllByFeedTitleOrderByPubdateDesc(String feedTitle);

    List<RssMessage> findAllByPubdateAndTitleAndAuthor(Date pubdate, String title, String author);

}
