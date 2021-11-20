package fr.lessagasmp3.core.rss.controller;

import fr.lessagasmp3.core.rss.entity.RssMessage;
import fr.lessagasmp3.core.rss.repository.RssMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class RssMessageController {

    @Autowired
    private RssMessageRepository rssMessageRepository;

    @RequestMapping(value = "/api/rss", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"feedTitle"})
    public Set<RssMessage> getByFeedTitle(@RequestParam String feedTitle) {
        return rssMessageRepository.findAllByFeedTitleOrderByPubdateDesc(feedTitle);
    }

    @RequestMapping(value = "/api/rss/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RssMessage getById(@PathVariable Long id) {
        return rssMessageRepository.findById(id).orElse(null);
    }

}
