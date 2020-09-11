package fr.lessagasmp3.core.controller;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/api")
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/health", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void health() {}

    @RequestMapping(value = "/test/notification", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, params = {"password"})
    public void testNotification(@RequestParam String password) {
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
            LOGGER.error("Error while sending notification to Firebase", e);
        }
        LOGGER.info("Successfully sent notification to Firebase: {}", response);
    }

}
