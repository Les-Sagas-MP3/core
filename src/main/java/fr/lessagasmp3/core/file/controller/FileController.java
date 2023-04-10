package fr.lessagasmp3.core.file.controller;

import fr.lessagasmp3.core.file.entity.File;
import fr.lessagasmp3.core.file.model.FileModel;
import fr.lessagasmp3.core.file.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/files/audio/**", method = RequestMethod.GET, produces = {"audio/mpeg", "audio/mpeg3"})
    public @ResponseBody byte[] getAudioFile(HttpServletRequest request) throws IOException {
        return fileService.readOnFilesystem(request);
    }

    @RequestMapping(value = "/files/image/**", method = RequestMethod.GET, produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody byte[] getImageFile(HttpServletRequest request) throws IOException {
        return fileService.readOnFilesystem(request);
    }

    @RequestMapping(value = "/file/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public FileModel getById(@PathVariable Long id) {
        return fileService.findInDbById(id);
    }

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
