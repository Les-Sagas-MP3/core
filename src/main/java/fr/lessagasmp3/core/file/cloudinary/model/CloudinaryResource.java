package fr.lessagasmp3.core.file.cloudinary.model;

import com.cloudinary.api.ApiResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class CloudinaryResource {

    private String url;

    private String publicId;

    public static CloudinaryResource fromApiResponse(ApiResponse response) {
        CloudinaryResource resource = new CloudinaryResource();
        resource.setUrl(response.get("secure_url").toString());
        resource.setPublicId(response.get("public_id").toString());
        return resource;
    }

}
