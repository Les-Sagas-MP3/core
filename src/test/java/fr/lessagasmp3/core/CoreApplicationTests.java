package fr.lessagasmp3.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@RunWith(SpringRunner.class)
@SpringBootTest
@Testcontainers
public class CoreApplicationTests {

	@Container
	public static final PostgreSQLContainer<?> database;

	static  {
		database = new PostgreSQLContainer<>("postgres:12-alpine")
				.withDatabaseName("lessagasmp3")
				.withUsername("lessagasmp3")
				.withPassword("lessagasmp3");
		database.start();
	}
	@DynamicPropertySource
	public static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.username", database::getUsername);
		registry.add("spring.datasource.password", database::getPassword);
		registry.add("spring.datasource.url", database::getJdbcUrl);
	}

	@Test
	public void contextLoads() {
	}

}
