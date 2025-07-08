package com.example.holiday_hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HolidayHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(HolidayHubApplication.class, args);
	}

}
