package com.service_desk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceDeskApplication {
	public static void main(String[] args)
	{
		System.setProperty("spring.profiles.active", "dev");
		SpringApplication.run(ServiceDeskApplication.class, args);
	}

}
