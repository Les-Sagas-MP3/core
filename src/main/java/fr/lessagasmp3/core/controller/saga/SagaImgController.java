package fr.lessagasmp3.core.controller.saga;

import fr.lessagasmp3.core.controller.FileController;
import fr.lessagasmp3.core.entity.Saga;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.repository.SagaRepository;
import fr.lessagasmp3.core.service.ImgurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/saga")
public class SagaImgController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SagaImgController.class);

    @Autowired
    private ImgurService imgurService;

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
        String filePath = fileController.upload(multipartFile, "tmp/" + sagaId, "cover", false);

        // Execute end of procedure in a separate thread
        new Thread(() -> {

            // Upload file to third-party
            File coverFile = new File(filePath);
            boolean success = false;
            while(!success) {
                LOGGER.debug("Trying to upload {} to imgur", coverFile.getName());
                try {
                    TimeUnit.SECONDS.sleep(10);
                    entity.setCoverUrl(imgurService.upload(coverFile, entity.getImgurAlbumHash(), "Cover"));
                    success = true;
                } catch (InterruptedException | IllegalStateException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }

            // Save infos
            sagaRepository.save(entity);

            // Delete temp file
            if(!coverFile.delete()) {
                LOGGER.error("Cannot delete {} file", filePath);
            }

        }).start();

    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/{id}/banner", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void uploadBanner(@PathVariable("id") Long sagaId, @RequestParam("file") MultipartFile multipartFile) {

        // Control & create album
        Saga entity = initUpload(sagaId, multipartFile);

        // Save File locally in a temporary folder
        String filePath = fileController.upload(multipartFile, "tmp/" + sagaId, "banner", false);

        // Execute end of procedure in a separate thread
        new Thread(() -> {

            // Upload file to third-party
            File coverFile = new File(filePath);
            boolean success = false;
            while(!success) {
                LOGGER.debug("Trying to upload {} to imgur", coverFile.getName());
                try {
                    TimeUnit.SECONDS.sleep(10);
                    entity.setBannerUrl(imgurService.upload(coverFile, entity.getImgurAlbumHash(), "Banner"));
                    success = true;
                } catch (InterruptedException | IllegalStateException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }

            // Save infos
            sagaRepository.save(entity);

            // Delete temp file
            if(!coverFile.delete()) {
                LOGGER.error("Cannot delete {} file", filePath);
            }

        }).start();

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

        // If album does not exist, create one
        if(entity.getImgurAlbumHash().isEmpty()) {
            entity.setImgurAlbumHash(imgurService.createAlbum(entity.getTitle()));
        }

        return entity;
    }

}
