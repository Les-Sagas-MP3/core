package fr.lessagasmp3.core.file.cloudinary.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class CloudinaryNotification {

    private String notification_type;
    private String secure_url;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CloudinaryNotification{");
        sb.append("notification_type='").append(notification_type).append('\'');
        sb.append(", secure_url='").append(secure_url).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
