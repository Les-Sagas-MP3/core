package fr.lessagasmp3.core.controller;

import fr.lessagasmp3.core.exception.ForbiddenException;
import fr.lessagasmp3.core.rss.Feed;
import fr.lessagasmp3.core.rss.FeedMessage;
import fr.lessagasmp3.core.rss.FeedParser;
import fr.lessagasmp3.core.scrapper.SagaScrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SyncController {

    @Value("${fr.lessagasmp3.core.adminpassword}")
    private String adminPassword;

    @Value("${fr.lessagasmp3.core.newsurl}")
    private String newsUrl;

    @Autowired
    private SagaScrapper sagaScrapper;

    @Autowired
    private TaskExecutor taskExecutor;

    @RequestMapping(value = "/api/sync/sagas", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, params = {"password"})
    public void syncSagas(@RequestParam String password) {
        if(adminPassword.equals(password)) {
            taskExecutor.execute(() -> sagaScrapper.scrap());
        } else {
            throw new ForbiddenException();
        }
    }

    @RequestMapping(value = "/api/sync/news", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, params = {"password"})
    public void syncNews(@RequestParam String password) {
        if(adminPassword.equals(password)) {
            taskExecutor.execute(() -> {
                FeedParser parser = new FeedParser(newsUrl);
                Feed feed = parser.readFeed();
                System.out.println(feed);
                for (FeedMessage message : feed.getEntries()) {
                    System.out.println(message);
                }
            });
        } else {
            throw new ForbiddenException();
        }
    }


}
