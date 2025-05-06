package com.example.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ThinhApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThinhApplication.class, args);
	}

}
