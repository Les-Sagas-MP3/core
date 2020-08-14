package fr.lessagasmp3.core;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import fr.lessagasmp3.core.controller.FileController;
import fr.lessagasmp3.core.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class CoreApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreApplication.class);
	private static final String GOOGLE_APPLICATION_CREDENTIALS = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
	private static final String FIREBASE_URL = System.getenv("FIREBASE_URL");

	@Autowired
	private FileController fileController;

	@Autowired
	private FileRepository fileRepository;

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		if(FIREBASE_URL != null) {
			if(GOOGLE_APPLICATION_CREDENTIALS != null) {
				LOGGER.info("Loading Firebase key from {}", GOOGLE_APPLICATION_CREDENTIALS);
				File googleAppCredentialsFile = new File(GOOGLE_APPLICATION_CREDENTIALS);
				if(googleAppCredentialsFile.exists()) {
					try {
						FirebaseOptions options = new FirebaseOptions.Builder()
								.setCredentials(GoogleCredentials.getApplicationDefault())
								.setDatabaseUrl(FIREBASE_URL)
								.build();
						FirebaseApp.initializeApp(options);
					} catch (IOException e) {
						LOGGER.error("Cannot initialize Firebase options", e);
					}
				} else {
					LOGGER.warn("The file {} does not exist", GOOGLE_APPLICATION_CREDENTIALS);
					loadGoogleApplicationCredentialsFromDb();
				}
			} else {
				loadGoogleApplicationCredentialsFromDb();
			}
		}
	}

	public void loadGoogleApplicationCredentialsFromDb() {
		LOGGER.info("Loading Firebase key from database");
		fr.lessagasmp3.core.entity.File file = fileRepository.findByDirectoryAndName("config", "GOOGLE_APPLICATION_CREDENTIALS");
		if(file == null) {
			LOGGER.error("No Firebase key stored in database");
		} else {
			fileController.prepareDirectories(file.getDirectory());
			Path fileName = Path.of(file.getPath());
			try {
				Files.writeString(fileName, file.getContent());
				FileInputStream refreshToken = new FileInputStream(file.getPath());
				FirebaseOptions options = new FirebaseOptions.Builder()
						.setCredentials(GoogleCredentials.fromStream(refreshToken))
						.setDatabaseUrl(FIREBASE_URL)
						.build();
				FirebaseApp.initializeApp(options);
			} catch (IOException e) {
				LOGGER.error("Cannot write {}", fileName, e);
			}
		}
	}

}
