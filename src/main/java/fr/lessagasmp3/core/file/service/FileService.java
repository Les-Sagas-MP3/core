package fr.lessagasmp3.core.file.service;

import fr.lessagasmp3.core.common.constant.MimeTypes;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.file.cloudinary.model.CloudinaryResource;
import fr.lessagasmp3.core.file.cloudinary.service.CloudinaryService;
import fr.lessagasmp3.core.file.entity.File;
import fr.lessagasmp3.core.file.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;

@Slf4j
@Service
public class FileService {

    @Value("${fr.lessagasmp3.core.storage}")
    private String storageFolder;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public File findInDbById(Long id) {
        return fileRepository.findById(id).orElse(null);
    }

    public byte[] readOnFilesystem(HttpServletRequest request) throws IOException {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String matchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String pathFile = new AntPathMatcher().extractPathWithinPattern(matchPattern, path);
        pathFile = pathFile.replaceAll("/files/audio", "");
        pathFile = pathFile.replaceAll("/", Matcher.quoteReplacement(java.io.File.separator));
        InputStream in = new FileInputStream(storageFolder + java.io.File.separator + pathFile);
        log.debug("Getting image {}", storageFolder + java.io.File.separator + pathFile);
        byte[] fileContent = IOUtils.toByteArray(in);
        in.close();
        return fileContent;
    }

    public File saveOnFilesystem(MultipartFile multipartFile, String directory, String name, boolean saveContentInDb) throws IOException {

        String fullPath;
        String finalFileName;

        InputStream inputStream = multipartFile.getInputStream();
        log.debug("inputStream: " + inputStream);
        String originalName = multipartFile.getOriginalFilename();
        log.debug("originalName: " + originalName);
        String contentType = multipartFile.getContentType();
        log.debug("contentType: " + contentType);
        long size = multipartFile.getSize();
        log.debug("size: " + size);
        String format = MimeTypes.getDefaultExt(contentType);
        log.debug("format: " + format);
        finalFileName = name + "." + format;
        log.debug("saved filename: " + finalFileName);

        prepareDirectories(directory);

        fullPath = storageFolder + java.io.File.separator + directory + java.io.File.separator + finalFileName;
        java.io.File file = new java.io.File(fullPath);
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
        }

        File entity = new File();
        if (saveContentInDb) {
            entity.setContent(Files.readString(Paths.get(file.getAbsolutePath())));
        }
        entity.setDirectory(directory);
        entity.setName(name);
        entity.setFormat(format);
        entity.setUrl("/" + directory + "/" + finalFileName);

        return entity;
    }

    public fr.lessagasmp3.core.file.entity.File saveInDb(File newEntity) {
        File entity = null;
        if(newEntity.getId() > 0) {
            entity = fileRepository.findById(newEntity.getId()).orElse(null);
        }
        if(entity == null) {
            entity = new File();
        }
        entity.setDirectory(newEntity.getDirectory());
        entity.setName(newEntity.getName());
        entity.setFormat(newEntity.getFormat());
        entity.setUrl(newEntity.getUrl());
        return fileRepository.save(entity);
    }

    public Boolean delete(Long fileId) {

        // Check if ID is correct
        if(fileId <= 0) {
            log.error("Impossible to delete file : ID is missing");
            throw new BadRequestException();
        }

        // Check if file is registered in DB
        File entity = fileRepository.findById(fileId).orElse(null);
        if(entity == null) {
            log.error("Impossible to delete file : file {} does not exist in DB", fileId);
            throw new NotFoundException();
        }

        // Manage third parties
        if(cloudinaryService.isEnabled()) {
            CloudinaryResource resource = cloudinaryService.find(entity);
            if(resource == null) {
                log.error("Impossible to delete file : file {} does not exist in Cloudinary", entity.getDirectory() + "/" + entity.getName());
            } else {
                cloudinaryService.delete(resource);
            }
        }

        // Delete file in filesystem
        java.io.File file = new java.io.File(getPath(entity));
        if(file.exists()) {
            if(!file.delete()) {
                log.error("Impossible to delete file : unknown error");
            }
        } else {
            log.error("Impossible to delete file : file {} does not exist in filesystem", fileId);
        }

        // Delete file in DB
        fileRepository.delete(entity);

        return Boolean.TRUE;
    }

    public void prepareDirectories(String directoryPath) {
        java.io.File directory = new java.io.File(storageFolder);
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
            directory = new java.io.File(storageFolder + java.io.File.separator + directoryPath.replaceAll("//", java.io.File.separator));
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

    public String getPath(File entity) {
        return storageFolder + java.io.File.separator + entity.getDirectory() + java.io.File.separator + entity.getFullname();
    }
}
