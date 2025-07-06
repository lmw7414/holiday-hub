package com.example.holiday_hub.initializer;

import com.example.holiday_hub.dto.CountryInfoDto;
import com.example.holiday_hub.dto.HolidayInfoDto;
import com.example.holiday_hub.repository.CountryInfoRepository;
import com.example.holiday_hub.repository.HolidayInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HolidaySyncInitializer {
    private final HolidayApiClient client;
    private final CountryInfoRepository countryInfoRepository;
    private final HolidayInfoRepository holidayInfoRepository;

    @Transactional
    public void syncAll(int fromYear, int toYear) {
        // TODO: 속도 개선 작업 REFACTORING 필요
        List<CountryInfoDto> countriesDtos = client.fetchCountries();
        countryInfoRepository.saveAll(countriesDtos.stream().map(CountryInfoDto::toEntity).toList());
        log.info("국가 정보 업로드 완료: {}개",countriesDtos.size());
        List<HolidayInfoDto> holidayInfoDtos = new ArrayList<>();
        for (int year = fromYear; year <= toYear; year++) {
            for (CountryInfoDto country : countriesDtos) {
                holidayInfoDtos.addAll(client.fetchHolidays(year, country.countryCode()));
            }
            log.info("{}년 정보 업데이트 완료", year);
        }
        holidayInfoRepository.saveAll(holidayInfoDtos.stream().map(HolidayInfoDto::toEntity).toList());
    }


}
