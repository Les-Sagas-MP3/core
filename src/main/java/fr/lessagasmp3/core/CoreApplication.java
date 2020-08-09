package fr.lessagasmp3.core;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;

@SpringBootApplication
public class CoreApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreApplication.class);
	private static final String GOOGLE_APPLICATION_CREDENTIALS = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		LOGGER.info("Loading Firebase key from {}", GOOGLE_APPLICATION_CREDENTIALS);
		try {
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.getApplicationDefault())
					.setDatabaseUrl("https://les-sagas-mp3.firebaseio.com")
					.build();
			FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			LOGGER.error("Cannot initialize Firebase options", e);
		}
	}

}
