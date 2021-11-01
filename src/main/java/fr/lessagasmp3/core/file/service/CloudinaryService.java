package fr.lessagasmp3.core.file.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import fr.lessagasmp3.core.constant.Urls;
import fr.lessagasmp3.core.file.entity.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class CloudinaryService {

    private Cloudinary connector;

    @Value("${fr.lessagasmp3.core.url}")
    private String apiUrl;

    @Value("${cloudinary.notification_endpoint}")
    private String notificationEndpoint;

    @Autowired
    private FileService fileService;

    public void init(String cloudinaryUrl) {
        Map<String, String> urlSplit = Urls.splitUrl(cloudinaryUrl);
        connector = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", urlSplit.get(Urls.HOST),
                "api_key", urlSplit.get(Urls.USERNAME),
                "api_secret", urlSplit.get(Urls.PASSWORD),
                "secure", true));
        log.info("Cloudinary initialized");
        log.debug("Cloudinary name : {}", urlSplit.get(Urls.HOST));
        log.debug("Cloudinary API Key : {}", urlSplit.get(Urls.USERNAME));
        log.debug("Cloudinary API Secret : {}", urlSplit.get(Urls.PASSWORD));
    }

    @Async
    public CompletableFuture<Void> upload(File entity) throws IOException {
        Map params = ObjectUtils.asMap(
                "public_id", entity.getDirectory(),
                "overwrite", true,
                "notification_url", apiUrl + notificationEndpoint
        );
        Map uploadResult = connector.uploader().upload(new java.io.File(fileService.getPath(entity)), params);
        return CompletableFuture.completedFuture(null);
    }

    public boolean isEnabled() {
        return connector != null;
    }
}
