package com.example.holiday_hub.initializer;

import com.example.holiday_hub.dto.CountryInfoDto;
import com.example.holiday_hub.dto.HolidayInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HolidayApiClient {

    static final String BASE_URL = "https://date.nager.at/api/v3";
    private final RestClient restClient;

    public List<CountryInfoDto> fetchCountries() {
        return restClient.get()
                .uri(BASE_URL + "/AvailableCountries")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public List<HolidayInfoDto> fetchHolidays(int year, String countryCode) {
        return restClient.get()
                .uri(BASE_URL + "/PublicHolidays" + "/{year}/{countryCode}", year, countryCode)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
