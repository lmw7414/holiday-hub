package com.example.holiday_hub;

import com.example.holiday_hub.initializer.HolidaySyncInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
@SpringBootApplication
public class HolidayHubApplication {

	private final HolidaySyncInitializer holidaySyncInitializer;

	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		holidaySyncInitializer.syncAll(2024, 2025);
	}

	public static void main(String[] args) {
		SpringApplication.run(HolidayHubApplication.class, args);
	}

}
