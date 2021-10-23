package fr.lessagasmp3.core.controller;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/api")
public class MainController {

    private static final String FIREBASE_URL = System.getenv("FIREBASE_URL");

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/health", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void health() {}

    @RequestMapping(value = "/test/notification", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public void testNotification() {
        if(FIREBASE_URL != null) {
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
            log.info("Successfully sent notification to Firebase: {}", response);
        }
    }

}
