package fr.lessagasmp3.core.file.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CloudinaryController {

    @RequestMapping(value = "${cloudinary.notification_endpoint}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllByIds(@RequestBody String body) {
        log.debug(body);
        return body;
    }

}
