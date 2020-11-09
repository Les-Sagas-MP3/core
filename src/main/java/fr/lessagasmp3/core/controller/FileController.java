package fr.lessagasmp3.core.controller;

import fr.lessagasmp3.core.constant.MimeTypes;
import fr.lessagasmp3.core.constant.Strings;
import fr.lessagasmp3.core.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
public class FileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileRepository fileRepository;

    @RequestMapping(value = "/file/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile,
                                             @RequestParam("directory") String directoryPath,
                                             @RequestParam("name") String name,
                                             @RequestParam(name = "saveInDb", value = "true", required = false) Boolean saveInDb) {
        if (multipartFile == null) {
            throw new RuntimeException("You must select the a file for uploading");
        }

        LOGGER.debug("name: " + name);
        String finalFileName = multipartFile.getOriginalFilename();
        String fullPath = Strings.EMPTY;
        try {
            InputStream inputStream = multipartFile.getInputStream();
            LOGGER.debug("inputStream: " + inputStream);
            String originalName = multipartFile.getOriginalFilename();
            LOGGER.debug("originalName: " + originalName);
            String contentType = multipartFile.getContentType();
            LOGGER.debug("contentType: " + contentType);
            long size = multipartFile.getSize();
            LOGGER.debug("size: " + size);
            String extension = "." + MimeTypes.getDefaultExt(contentType);
            LOGGER.debug("extension: " + extension);
            finalFileName = name + extension;
            LOGGER.debug("saved filename: " + finalFileName);

            prepareDirectories(directoryPath);

            fullPath = "files/" + directoryPath + "/" + finalFileName;
            File file = new File(fullPath);
            try (OutputStream os = new FileOutputStream(file)) {
                os.write(multipartFile.getBytes());
            }

            if (saveInDb) {
                fr.lessagasmp3.core.entity.File entity = new fr.lessagasmp3.core.entity.File();
                entity.setDirectory(directoryPath);
                entity.setName(name);
                entity.setPath(file.getAbsolutePath());
                entity.setContent(Files.readString(Paths.get(file.getAbsolutePath())));
                fileRepository.save(entity);
            }

        } catch (IOException e) {
            LOGGER.error("Cannot save file {}", finalFileName, e);
        }

        return fullPath;
    }

    public void prepareDirectories(String directoryPath) {
        File directory = new File("files");
        if (!directory.exists()) {
            LOGGER.info("Creating path {}", directory.getPath());
            directory.mkdirs();
        }
        if (!directory.isDirectory()) {
            LOGGER.error("The path {} is not a directory", directory.getPath());
        }

        if (directoryPath != null && !directoryPath.isEmpty()) {
            directory = new File("files/" + directoryPath);
            if (!directory.exists()) {
                LOGGER.info("Creating path {}", directory.getPath());
                directory.mkdirs();
            }
            if (!directory.isDirectory()) {
                LOGGER.error("The path {} is not a directory", directory.getPath());
            }
        }
    }

}
