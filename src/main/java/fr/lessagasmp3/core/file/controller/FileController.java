package fr.lessagasmp3.core.file.controller;

import fr.lessagasmp3.core.file.entity.File;
import fr.lessagasmp3.core.file.model.FileModel;
import fr.lessagasmp3.core.file.service.CloudinaryService;
import fr.lessagasmp3.core.file.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private FileService fileService;

    @PostMapping(value = "/file/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public FileModel upload(@RequestParam(name = "file") MultipartFile multipartFile,
                            @RequestParam(name = "directory") String directory,
                            @RequestParam(name = "name") String name,
                            @RequestParam(name = "saveInDb", defaultValue = "false", required = false) Boolean saveInDb) {

        if (multipartFile == null) {
            throw new RuntimeException("You must add a file for uploading");
        }

        File entity = new File();
        try {
            entity = fileService.saveOnFilesystem(multipartFile, directory, name, saveInDb);
            entity = fileService.saveInDb(entity);
            if(cloudinaryService.isEnabled()) {
                cloudinaryService.upload(entity);
            }
        } catch (IOException e) {
            log.error("Cannot save file {}", multipartFile.getName(), e);
        }

        return FileModel.fromEntity(entity);
    }


}
