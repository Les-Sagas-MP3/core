package fr.lessagasmp3.core.scrapper;

import fr.lessagasmp3.core.model.RssMessage;
import fr.lessagasmp3.core.repository.RssMessageRepository;
import fr.lessagasmp3.core.rss.Feed;
import fr.lessagasmp3.core.rss.FeedParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class NewsScrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsScrapper.class);

    private static final String TITLE_SEPARATOR = " :: ";

    @Value("${fr.lessagasmp3.core.news.rss.title}")
    private String rssTitle;

    @Value("${fr.lessagasmp3.core.news.rss.url}")
    private String rssUrl;

    @Autowired
    private RssMessageRepository rssMessageRepository;

    public void scrap() {
        LOGGER.debug("Download RSS feed : {}", rssUrl);
        FeedParser parser = new FeedParser(rssUrl);
        Feed feed = parser.readFeed();
        LOGGER.debug("{} entries found", feed.getEntries().size());
        for (RssMessage message : feed.getEntries()) {
            message.setFeedTitle(rssTitle);
            String[] splitTitle = message.getTitle().split(TITLE_SEPARATOR);
            if(splitTitle.length >= 2) {
                message.setTitle(splitTitle[1]);
            }
            Pattern pattern = Pattern.compile(".*\\((.*)\\)");
            Matcher matcher = pattern.matcher(message.getAuthor());
            while (matcher.find()) {
                message.setAuthor(matcher.group(1));
            }
            List<RssMessage> messages = rssMessageRepository.findAllByPubdateAndTitleAndAuthor(message.getPubdate(), message.getTitle(), message.getAuthor());
            int messagesSize = messages.size();
            if(messagesSize == 0) {
                rssMessageRepository.save(message);
            } else if(messagesSize == 1) {
                RssMessage messageInDb = messages.get(0);
                messageInDb.setDescription(message.getDescription());
                messageInDb.setLink(message.getLink());
                messageInDb.setGuid(message.getGuid());
                rssMessageRepository.save(messageInDb);
            } else {
                LOGGER.error("There are more than 1 RSS messages \"{}\" written by {} published on {}", message.getTitle(), message.getAuthor(), message.getPubdate());
            }
        }
    }

}
