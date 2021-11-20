package fr.lessagasmp3.core.common.controller;

import fr.lessagasmp3.core.common.scrapper.NewsScrapper;
import fr.lessagasmp3.core.common.scrapper.SagaScrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SyncController {

    @Autowired
    private NewsScrapper newsScrapper;

    @Autowired
    private SagaScrapper sagaScrapper;

    @Autowired
    private TaskExecutor taskExecutor;

    @RequestMapping(value = "/api/sync/sagas", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public void syncSagas() {
        taskExecutor.execute(() -> sagaScrapper.scrap());
    }

    @RequestMapping(value = "/api/sync/news", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public void syncNews() {
        taskExecutor.execute(() -> newsScrapper.scrap());
    }

    @Scheduled(cron = "0 0 */6 * * ?", zone = "Europe/Paris")
    public void scheduleSyncSagas() {
        log.info("Starting synchronization of sagas");
        syncSagas();
    }

    @Scheduled(cron = "0 * * * * ?", zone = "Europe/Paris")
    public void scheduleSyncNews() {
        log.info("Starting synchronization of news");
        syncNews();
    }

}
