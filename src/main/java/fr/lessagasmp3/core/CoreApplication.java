package fr.lessagasmp3.core;

import fr.lessagasmp3.core.scrapper.SagaScrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class CoreApplication {

	@Autowired
	private SagaScrapper sagaScrapper;

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		//sagaScrapper.scrap();
	}
}
