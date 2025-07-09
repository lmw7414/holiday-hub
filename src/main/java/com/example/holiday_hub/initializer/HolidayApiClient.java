package com.example.holiday_hub.initializer;

import com.example.holiday_hub.dto.CountryInfoDto;
import com.example.holiday_hub.dto.HolidayInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HolidayApiClient {

    static final String BASE_URL = "https://date.nager.at/api/v3";
    private final RestClient restClient;

    @Retryable(
            maxAttempts = 2,
            backoff = @Backoff(delay = 1000L)
    )
    public List<CountryInfoDto> fetchCountries() {
        return restClient.get()
                .uri(BASE_URL + "/AvailableCountries")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Retryable(
            maxAttempts = 2,
            backoff = @Backoff(delay = 1000L)
    )
    public List<HolidayInfoDto> fetchHolidays(int year, String countryCode) {
        return restClient.get()
                .uri(BASE_URL + "/PublicHolidays" + "/{year}/{countryCode}", year, countryCode)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Recover
    public List<HolidayInfoDto> recover(Exception e, int year, String countryCode) {
        log.error("[fetchHoliday]Retry 실패 year = {} countryCode={}", year, countryCode);
        return List.of();
    }

    @Recover
    public List<CountryInfoDto> recover(Exception e) {
        log.error("[fetchCountries]Retry 실패");
        return List.of();
    }

}
