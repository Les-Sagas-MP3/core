package fr.lessagasmp3.core.controller;

import fr.lessagasmp3.core.scrapper.NewsScrapper;
import fr.lessagasmp3.core.scrapper.SagaScrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @Scheduled(cron = "0 0 */6 * * *")
    public void scheduleSyncSagas() {
        syncSagas();
    }

    @Scheduled(cron = "0 0 */6 * * *")
    public void scheduleSyncNews() {
        syncNews();
    }

}
