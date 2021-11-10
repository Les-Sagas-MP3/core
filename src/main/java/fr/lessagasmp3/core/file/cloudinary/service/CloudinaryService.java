package fr.lessagasmp3.core.file.cloudinary.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import fr.lessagasmp3.core.constant.HttpProxy;
import fr.lessagasmp3.core.constant.Strings;
import fr.lessagasmp3.core.constant.Urls;
import fr.lessagasmp3.core.file.cloudinary.model.CloudinaryResource;
import fr.lessagasmp3.core.file.core.entity.File;
import fr.lessagasmp3.core.file.core.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

        if(!Objects.equals(HttpProxy.HTTP_HOST, Strings.EMPTY)) {
            connector.config.proxyHost = HttpProxy.HTTP_HOST;
            connector.config.proxyPort = HttpProxy.HTTP_PORT;
        }
        log.info("Cloudinary initialized");
        log.debug("Cloudinary name : {}", urlSplit.get(Urls.HOST));
        log.debug("Cloudinary API Key : {}", urlSplit.get(Urls.USERNAME));
        log.debug("Cloudinary API Secret : {}", urlSplit.get(Urls.PASSWORD));
    }

    @Async
    public CompletableFuture<Void> upload(File entity) throws IOException {
        Map params = ObjectUtils.asMap(
                "public_id", entity.getDirectory() + "/" + entity.getName(),
                "overwrite", true,
                "notification_url", apiUrl + notificationEndpoint + "?id=" + entity.getId()
        );
        Map uploadResult = connector.uploader().upload(new java.io.File(fileService.getPath(entity)), params);
        return CompletableFuture.completedFuture(null);
    }

    public CloudinaryResource find(File entity) {
        CloudinaryResource resource = null;
        String publicId = entity.getDirectory() + "/" + entity.getName();
        try {
            log.debug("Finding Cloudinary resource : {}", publicId);
            ApiResponse response = connector.api().resource(publicId, null);
            log.debug("Response : {}", response.toString());
            resource = CloudinaryResource.fromApiResponse(response);
        } catch (Exception e) {
            log.error("Unable to get resource {} from Cloudinary", publicId);
        }
        return resource;
    }

    public void delete(CloudinaryResource resource) {
        List<String> publicIds = new ArrayList<>();
        publicIds.add(resource.getPublicId());
        try {
            log.debug("Deleting Cloudinary resource : {}", resource.getPublicId());
            connector.api().deleteResources(publicIds, null);
            log.debug("Cloudinary resource {} deleted", resource.getPublicId());
        } catch (Exception e) {
            log.error("Unable to delete Cloudinary resource {}", resource.getPublicId());
        }
    }

    public boolean isEnabled() {
        return connector != null;
    }

}
