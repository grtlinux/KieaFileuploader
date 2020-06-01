package org.tain;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.tain.properties.StorageProperties;
import org.tain.service.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class KieaFileuploaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(KieaFileuploaderApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}
}
