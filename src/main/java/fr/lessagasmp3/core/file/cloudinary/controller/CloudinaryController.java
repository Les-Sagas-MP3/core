package fr.lessagasmp3.core.file.cloudinary.controller;

import fr.lessagasmp3.core.file.entity.File;
import fr.lessagasmp3.core.file.cloudinary.model.CloudinaryNotification;
import fr.lessagasmp3.core.file.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class CloudinaryController {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "${cloudinary.notification_endpoint}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void notify(@RequestParam Long id, @RequestBody CloudinaryNotification notification) {
        File file = fileService.findInDbById(id);
        if(file != null) {
            file.setUrl(notification.getSecure_url());
            fileService.saveInDb(file);
        }
    }

}
