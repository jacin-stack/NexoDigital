package com.nexodigital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.nexodigital")
public class NexoDigitalBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(NexoDigitalBackendApplication.class, args);
	}

}
