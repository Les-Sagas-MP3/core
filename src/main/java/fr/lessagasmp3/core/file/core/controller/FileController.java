package fr.lessagasmp3.core.file.core.controller;

import fr.lessagasmp3.core.file.core.entity.File;
import fr.lessagasmp3.core.file.core.model.FileModel;
import fr.lessagasmp3.core.file.cloudinary.service.CloudinaryService;
import fr.lessagasmp3.core.file.core.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
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

    @DeleteMapping(value = "/file/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean delete(@PathVariable("id") Long id) {
        return fileService.delete(id);
    }


}
