package fr.lessagasmp3.core.controller.saga;

import fr.lessagasmp3.core.file.core.controller.FileController;
import fr.lessagasmp3.core.entity.Saga;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.file.core.model.FileModel;
import fr.lessagasmp3.core.repository.SagaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/saga")
public class SagaImgController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SagaImgController.class);

    @Autowired
    private FileController fileController;

    @Autowired
    private SagaRepository sagaRepository;

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/{id}/cover", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void uploadCover(@PathVariable("id") Long sagaId, @RequestParam("file") MultipartFile multipartFile) {

        // Control & create album
        Saga entity = initUpload(sagaId, multipartFile);

        // Save File locally in a temporary folder
        FileModel fileModel = fileController.upload(multipartFile, "img" + File.separator + sagaId, "cover", false);

        // Set relative file URL
        entity.setCoverUrl(fileModel.getUrl());
        LOGGER.debug(entity.getCoverUrl());
        sagaRepository.save(entity);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/{id}/banner", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void uploadBanner(@PathVariable("id") Long sagaId, @RequestParam("file") MultipartFile multipartFile) {

        // Control & create album
        Saga entity = initUpload(sagaId, multipartFile);

        // Save File locally in a temporary folder
        FileModel fileModel = fileController.upload(multipartFile, "img" + File.separator + sagaId, "banner", false);

        // Set relative file URL
        entity.setBannerUrl(fileModel.getUrl());
        LOGGER.debug(entity.getBannerUrl());
        sagaRepository.save(entity);
    }

    private Saga initUpload(Long sagaId, MultipartFile multipartFile) {

        // Verify that arguments are correct
        if(sagaId <= 0 || multipartFile == null) {
            LOGGER.error("Impossible to upload cover : arguments are incorrect");
            throw new BadRequestException();
        }

        // Verify that entity exists
        Saga entity = sagaRepository.findById(sagaId).orElse(null);
        if(entity == null) {
            LOGGER.error("Impossible to upload cover : saga {} not found", sagaId);
            throw new NotFoundException();
        }

        return entity;
    }

}
