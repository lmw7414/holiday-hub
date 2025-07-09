package com.example.holiday_hub.initializer;

import com.example.holiday_hub.dto.CountryInfoDto;
import com.example.holiday_hub.service.CountryService;
import com.example.holiday_hub.service.HolidayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HolidayInfoSyncScheduler {
    private final HolidayService holidayService;
    private final CountryService countryService;

    @Scheduled(cron = "0 0 1 2 1 *", zone = "Asia/Seoul")
    public void syncPreviousAndCurrentYear() {
        int year = Year.now().getValue();
        List<String> countryCodes = countryService.findAll().stream().map(CountryInfoDto::countryCode).toList();
        for(String code : countryCodes) {
            log.info("[공휴일 동기화] 국가: {}, 연도: {}", code, year);
            holidayService.updateHoliday(year, code);
            log.info("[공휴일 동기화] 국가: {}, 연도: {}", code, year - 1);
            holidayService.updateHoliday(year - 1, code);
        }
        log.info("[공휴일 동기화] {}년도 및 {}년도 공휴일 동기화 완료", year - 1, year);
    }
}
