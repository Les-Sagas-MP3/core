package fr.lessagasmp3.core.controller;

import fr.lessagasmp3.core.model.RssMessage;
import fr.lessagasmp3.core.repository.RssMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class RssMessageController {

    @Autowired
    private RssMessageRepository rssMessageRepository;

    @RequestMapping(value = "/api/rss", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"feedTitle"})
    public Set<RssMessage> getRssMessageByFeedTitle(@RequestParam String feedTitle) {
        return rssMessageRepository.findAllByFeedTitleOrderByIdDesc(feedTitle);
    }

}
