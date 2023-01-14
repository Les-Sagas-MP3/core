package fr.lessagasmp3.core;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import fr.lessagasmp3.core.auth.entity.Authority;
import fr.lessagasmp3.core.auth.repository.AuthorityRepository;
import fr.lessagasmp3.core.common.constant.AuthorityName;
import fr.lessagasmp3.core.file.repository.FileRepository;
import fr.lessagasmp3.core.file.service.FileService;
import fr.lessagasmp3.core.user.entity.User;
import fr.lessagasmp3.core.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class CoreApplication {

    private static final String GOOGLE_APPLICATION_CREDENTIALS = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
    private static final String FIREBASE_URL = System.getenv("FIREBASE_URL");

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {

        // First launch of App
        if (authorityRepository.count() == 0) {

            // Create authorities
            for (AuthorityName authorityName : AuthorityName.values()) {
                authorityRepository.save(new Authority(authorityName));
            }

            // Get user and admin authorities
            List<Authority> authorities = authorityRepository.findAll();
            Authority userAuthority = authorities.stream().filter(authority -> authority.getName().equals(AuthorityName.ROLE_USER)).findFirst().orElse(null);
            Authority adminAuthority = authorities.stream().filter(authority -> authority.getName().equals(AuthorityName.ROLE_ADMIN)).findFirst().orElse(null);
            if (userAuthority == null || adminAuthority == null) {
                throw new IllegalStateException();
            }

            // Create admin user
            String email = "admin@les-sagas-mp3.fr";
            //String generatedPassword = Strings.randomString();
            String generatedPassword = "admin@les-sagas-mp3.fr";
            User admin = new User(email, generatedPassword);
            admin.setUsername(email);
            admin.setEnabled(true);
            admin.addAuthority(userAuthority);
            admin.addAuthority(adminAuthority);
            admin.setEnabled(true);
            userRepository.save(admin);

            log.info("ONLY PRINTED ONCE - Default credentials are : admin@les-sagas-mp3.fr / {}", generatedPassword);
        }

        // Init Firebase connexion
        if (FIREBASE_URL != null) {
            if (GOOGLE_APPLICATION_CREDENTIALS != null) {
                log.info("Loading Firebase key from {}", GOOGLE_APPLICATION_CREDENTIALS);
                File googleAppCredentialsFile = new File(GOOGLE_APPLICATION_CREDENTIALS);
                if (googleAppCredentialsFile.exists()) {
                    try {
                        FirebaseOptions.Builder builder = FirebaseOptions.builder();
                        FirebaseOptions options = builder
                                .setCredentials(GoogleCredentials.getApplicationDefault())
                                .setDatabaseUrl(FIREBASE_URL)
                                .build();
                        FirebaseApp.initializeApp(options);
                    } catch (IOException e) {
                        log.error("Cannot initialize Firebase options", e);
                    }
                } else {
                    log.warn("The file {} does not exist", GOOGLE_APPLICATION_CREDENTIALS);
                    loadGoogleApplicationCredentialsFromDb();
                }
            } else {
                loadGoogleApplicationCredentialsFromDb();
            }
        }

    }

    public void loadGoogleApplicationCredentialsFromDb() {
        log.info("Loading Firebase key from database");
        fr.lessagasmp3.core.file.entity.File file = fileRepository.findByDirectoryAndName("config", "GOOGLE_APPLICATION_CREDENTIALS");
        if (file == null) {
            log.error("No Firebase key stored in database");
        } else {
            fileService.prepareDirectories(file.getDirectory());
            String path = fileService.getPath(file);
            Path fileName = Path.of(fileService.getPath(file));
            try {
                Files.writeString(fileName, file.getContent());
                FileInputStream refreshToken = new FileInputStream(path);
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(refreshToken))
                        .setDatabaseUrl(FIREBASE_URL)
                        .build();
                FirebaseApp.initializeApp(options);
            } catch (IOException e) {
                log.error("Cannot write {}", fileName, e);
            }
        }
    }

}
