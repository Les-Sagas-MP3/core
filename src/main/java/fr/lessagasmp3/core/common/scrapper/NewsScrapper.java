package fr.lessagasmp3.core.common.scrapper;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import fr.lessagasmp3.core.common.constant.EventLogName;
import fr.lessagasmp3.core.eventlog.entity.EventLog;
import fr.lessagasmp3.core.rss.entity.RssMessage;
import fr.lessagasmp3.core.eventlog.service.EventLogService;
import fr.lessagasmp3.core.rss.repository.RssMessageRepository;
import fr.lessagasmp3.core.rss.model.Feed;
import fr.lessagasmp3.core.rss.parser.FeedParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class NewsScrapper {

    private static final String FIREBASE_URL = System.getenv("FIREBASE_URL");
    private static final String TITLE_SEPARATOR = " :: ";

    @Value("${fr.lessagasmp3.core.news.rss.title}")
    private String rssTitle;

    @Value("${fr.lessagasmp3.core.news.rss.url}")
    private String rssUrl;

    @Autowired
    private EventLogService eventLogService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private RssMessageRepository rssMessageRepository;

    public void scrap() {
        log.info("Download RSS feed : {}", rssUrl);
        eventLogService.insert(new EventLog(EventLogName.SYNC_NEWS_START));
        eventLogService.rotate(EventLogName.SYNC_NEWS_START);
        FeedParser parser = new FeedParser(rssUrl);
        Feed feed = parser.readFeed();
        log.debug("{} entries found", feed.getEntries().size());
        boolean newRssMessage = false;
        for (RssMessage message : feed.getEntries()) {
            message.setFeedTitle(rssTitle);
            String[] splitTitle = message.getTitle().split(TITLE_SEPARATOR);
            if (splitTitle.length >= 2) {
                message.setTitle(splitTitle[1]);
            }
            Pattern pattern = Pattern.compile(".*\\((.*)\\)");
            Matcher matcher = pattern.matcher(message.getAuthor());
            while (matcher.find()) {
                message.setAuthor(matcher.group(1));
            }
            List<RssMessage> messages = rssMessageRepository.findAllByPubdateAndTitleAndAuthor(message.getPubdate(), message.getTitle(), message.getAuthor());
            int messagesSize = messages.size();
            if (messagesSize == 0) {
                newRssMessage = true;
                rssMessageRepository.save(message);
            } else if (messagesSize == 1) {
                RssMessage messageInDb = messages.get(0);
                messageInDb.setDescription(message.getDescription());
                messageInDb.setLink(message.getLink());
                messageInDb.setGuid(message.getGuid());
                rssMessageRepository.save(messageInDb);
            } else {
                log.error("There are more than 1 RSS messages \"{}\" written by {} published on {}", message.getTitle(), message.getAuthor(), message.getPubdate());
            }
        }

        // Send Firebase notification
        if (FIREBASE_URL != null && newRssMessage) {
            Message firebaseNotification = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(messageSource.getMessage("notification.news.title", null, Locale.getDefault()))
                            .setBody(messageSource.getMessage("notification.news.body", null, Locale.getDefault()))
                            .build())
                    .setTopic("news")
                    .build();

            String response = null;
            try {
                response = FirebaseMessaging.getInstance().send(firebaseNotification);
            } catch (FirebaseMessagingException e) {
                log.error("Error while sending notification to Firebase", e);
            }
            log.info("Successfully sent notification to Firebase: " + response);
        }
        eventLogService.insert(new EventLog(EventLogName.SYNC_NEWS_STOP));
        eventLogService.rotate(EventLogName.SYNC_NEWS_STOP);
        log.info("Download RSS feed ended");
    }

}
