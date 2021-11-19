package fr.lessagasmp3.core.pdf;

import fr.lessagasmp3.core.file.controller.FileController;
import fr.lessagasmp3.core.file.model.FileModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.Instant;

@Slf4j
@RestController
@RequestMapping("/api/saga")
public class SagaPdfController {

    @Autowired
    private FileController fileController;

    @Autowired
    private PdfScrapper pdfScrapper;

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/pdf", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void uploadPdf(@RequestParam("file") MultipartFile multipartFile) {
        Timestamp ts = Timestamp.from(Instant.now());
        FileModel fileModel = fileController.upload(multipartFile, "tmp", String.valueOf(ts.getTime()), false);
        pdfScrapper.scrap(fileModel);
        fileController.delete(fileModel.getId());
    }

}
