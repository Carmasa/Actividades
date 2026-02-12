package com.example.plataformasaas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PlataformaSaaSApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlataformaSaaSApplication.class, args);
	}

}
