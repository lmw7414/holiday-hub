package com.example.holiday_hub.initializer;

import com.example.holiday_hub.dto.CountryInfoDto;
import com.example.holiday_hub.dto.HolidayInfoDto;
import com.example.holiday_hub.repository.CountryInfoRepository;
import com.example.holiday_hub.repository.HolidayInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Component
@RequiredArgsConstructor
public class HolidaySyncInitializer {
    private final HolidayApiClient client;
    private final CountryInfoRepository countryInfoRepository;
    private final HolidayInfoRepository holidayInfoRepository;

    @Value("${support.year.from}")
    private Integer fromYear;
    @Value("${support.year.to}")
    private Integer toYear;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        List<String> countryCodes = syncCountry();
        syncAll(countryCodes, fromYear, toYear);
    }

    public void syncAll(List<String> countryCodes, int fromYear, int toYear) {
        ExecutorService excutor = Executors.newFixedThreadPool(20);
        List<Future<List<HolidayInfoDto>>> futures = new ArrayList<>();

        for (int year = fromYear; year <= toYear; year++) {
            for (String countryCode : countryCodes) {
                final int finalYear = year;
                futures.add(excutor.submit(() -> client.fetchHolidays(finalYear, countryCode)));
            }
            log.info("{}년 fetch 요청", year);
        }

        List<HolidayInfoDto> holidayInfoDtos = new ArrayList<>();
        for (Future<List<HolidayInfoDto>> future : futures) {
            try {
                holidayInfoDtos.addAll(future.get()); // future 작업 마칠 때까지 대기
            } catch (Exception e) {
                log.warn("공휴일 정보 조회 실패", e);
            }

        }
        excutor.shutdown();
        holidayInfoRepository.saveAll(holidayInfoDtos.stream().map(HolidayInfoDto::toEntity).toList());
        log.info("{}개 공휴일 DB 업데이트 성공", holidayInfoDtos.size());
    }

    public List<String> syncCountry() {
        List<CountryInfoDto> countriesDtos = client.fetchCountries();
        countryInfoRepository.saveAll(countriesDtos.stream().map(CountryInfoDto::toEntity).toList());
        log.info("국가 정보 업로드 완료: {}개", countriesDtos.size());
        return countriesDtos.stream().map(CountryInfoDto::countryCode).toList();
    }


}
