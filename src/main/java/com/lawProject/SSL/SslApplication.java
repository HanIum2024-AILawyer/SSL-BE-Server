package com.lawProject.SSL;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SslApplication {

	public static void main(String[] args) {
		SpringApplication.run(SslApplication.class, args);
	}

}
