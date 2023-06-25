package fr.lessagasmp3.core;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import fr.lessagasmp3.core.common.constant.Strings;
import fr.lessagasmp3.core.common.security.JwtRequest;
import fr.lessagasmp3.core.file.service.FileService;
import fr.lessagasmp3.core.role.entity.Role;
import fr.lessagasmp3.core.role.model.RoleName;
import fr.lessagasmp3.core.role.service.RoleService;
import fr.lessagasmp3.core.user.entity.User;
import fr.lessagasmp3.core.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@Slf4j
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class CoreApplication {

    private static final String GOOGLE_APPLICATION_CREDENTIALS = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
    private static final String FIREBASE_URL = System.getenv("FIREBASE_URL");

    @Value("${fr.lessagasmp3.core.admin_password}")
    private String adminPassword;

    private final FileService fileService;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public CoreApplication(
            FileService fileService,
            RoleService roleService,
            UserService userService) {
        this.fileService = fileService;
        this.roleService = roleService;
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {

        // First launch of App
        if (userService.count() == 0) {

            // Create admin user
            String email = "admin@les-sagas-mp3.fr";
            String generatedPassword = "admin@les-sagas-mp3.fr";
            if(adminPassword == null || adminPassword.isEmpty()) {
                generatedPassword = Strings.randomString();
            }
            JwtRequest jwtAdminRequest = new JwtRequest();
            jwtAdminRequest.setEmail(email);
            jwtAdminRequest.setPassword(generatedPassword);
            User admin = userService.create(jwtAdminRequest, "admin");

            // Set member role
            Role role = new Role();
            role.setName(RoleName.ADMINISTRATOR);
            role.setUser(admin);
            roleService.create(role);

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
        fr.lessagasmp3.core.file.entity.File file = fileService.findByDirectoryAndName("config", "GOOGLE_APPLICATION_CREDENTIALS");
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
