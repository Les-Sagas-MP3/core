package fr.lessagasmp3.core.controller;

import fr.lessagasmp3.core.exception.ForbiddenException;
import fr.lessagasmp3.core.scrapper.NewsScrapper;
import fr.lessagasmp3.core.scrapper.SagaScrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SyncController {

    @Value("${fr.lessagasmp3.core.adminpassword}")
    private String adminPassword;

    @Autowired
    private NewsScrapper newsScrapper;

    @Autowired
    private SagaScrapper sagaScrapper;

    @Autowired
    private TaskExecutor taskExecutor;

    @RequestMapping(value = "/api/sync/sagas", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, params = {"password"})
    public void syncSagas(@RequestParam String password) {
        if(adminPassword.equals(password)) {
            taskExecutor.execute(() -> sagaScrapper.scrap());
        } else {
            throw new ForbiddenException();
        }
    }

    @RequestMapping(value = "/api/sync/news", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, params = {"password"})
    public void syncNews(@RequestParam String password) {
        if(adminPassword.equals(password)) {
            taskExecutor.execute(() -> newsScrapper.scrap());
        } else {
            throw new ForbiddenException();
        }
    }

    @Scheduled(cron = "0 0 */6 * * *")
    public void syncSagas() {
        syncSagas(adminPassword);
    }

    @Scheduled(cron = "0 0 */6 * * *")
    public void syncNews() {
        syncNews(adminPassword);
    }

}
