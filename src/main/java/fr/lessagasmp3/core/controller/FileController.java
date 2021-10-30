package fr.lessagasmp3.core.controller;

import fr.lessagasmp3.core.constant.MimeTypes;
import fr.lessagasmp3.core.constant.Strings;
import fr.lessagasmp3.core.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    private FileRepository fileRepository;

    @Value("${fr.lessagasmp3.core.storage}")
    private String storageFolder;

    @PostMapping(value = "/file/upload", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public String upload(@RequestParam(name = "file") MultipartFile multipartFile,
                             @RequestParam(name = "directory") String directory,
                             @RequestParam(name = "name") String name,
                             @RequestParam(name = "saveInDb", defaultValue = "false", required = false) Boolean saveInDb) {

        if (multipartFile == null) {
            throw new RuntimeException("You must select the a file for uploading");
        }

        log.debug("name: " + name);
        String finalFileName = multipartFile.getOriginalFilename();
        String fullPath = Strings.EMPTY;
        try {
            InputStream inputStream = multipartFile.getInputStream();
            log.debug("inputStream: " + inputStream);
            String originalName = multipartFile.getOriginalFilename();
            log.debug("originalName: " + originalName);
            String contentType = multipartFile.getContentType();
            log.debug("contentType: " + contentType);
            long size = multipartFile.getSize();
            log.debug("size: " + size);
            String extension = "." + MimeTypes.getDefaultExt(contentType);
            log.debug("extension: " + extension);
            finalFileName = name + extension;
            log.debug("saved filename: " + finalFileName);

            prepareDirectories(directory);

            fullPath = storageFolder + File.separator + directory + File.separator + finalFileName;
            File file = new File(fullPath);
            try (OutputStream os = new FileOutputStream(file)) {
                os.write(multipartFile.getBytes());
            }

            fr.lessagasmp3.core.entity.File entity = new fr.lessagasmp3.core.entity.File();
            if (saveInDb) {
                entity.setContent(Files.readString(Paths.get(file.getAbsolutePath())));
            }
            entity.setDirectory(directory);
            entity.setName(name);
            entity.setPath(file.getAbsolutePath());
            fileRepository.save(entity);

        } catch (IOException e) {
            log.error("Cannot save file {}", finalFileName, e);
        }

        return fullPath;
    }

    public void prepareDirectories(String directoryPath) {
        File directory = new File(storageFolder);
        if (!directory.exists()) {
            log.info("Creating path {}", directory.getPath());
            if (!directory.mkdirs()) {
                log.error("The path {} cannot be created", directory.getPath());
            }
        }
        if (!directory.isDirectory()) {
            log.error("The path {} is not a directory", directory.getPath());
        }

        if (directoryPath != null && !directoryPath.isEmpty()) {
            directory = new File(storageFolder + File.separator + directoryPath.replaceAll("/", File.separator));
            log.debug("Prepare directory {}", directory.getAbsolutePath());
            if (!directory.exists()) {
                log.info("Creating path {}", directory.getPath());
                if (!directory.mkdirs()) {
                    log.error("Cannot create directory {}", directory.getAbsolutePath());
                }
            }
            if (!directory.isDirectory()) {
                log.error("The path {} is not a directory", directory.getPath());
            }
        }
    }

}
