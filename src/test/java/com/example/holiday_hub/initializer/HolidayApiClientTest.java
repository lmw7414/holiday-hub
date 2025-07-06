package com.example.holiday_hub.initializer;

import com.example.holiday_hub.dto.CountryInfoDto;
import com.example.holiday_hub.dto.HolidayInfoDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.List;

class HolidayApiClientTest {
    static final String countryApi = "https://date.nager.at/api/v3/AvailableCountries";
    static final String publicHolidayApi = "https://date.nager.at/api/v3/PublicHolidays";

    @Test
    void 나라_정보_가져오기() {
        RestClient restClient = RestClient.builder().build();
        List<CountryInfoDto> result = restClient.get().uri(countryApi).retrieve().body(new ParameterizedTypeReference<>() {
        });
        Assertions.assertNotNull(result);
        System.out.println("나라 개수: " + result.size());
        for (CountryInfoDto dto : result) {
            System.out.println(dto);
        }
    }

    /*
    {
        "date": "2075-01-01",
        "localName": "새해",
        "name": "New Year's Day",
        "countryCode": "KR",
        "fixed": false,
        "global": true,
        "counties": null,
        "launchYear": null,
        "types": [
            "Public"
        ]
    }
   */
    @Test
    void 특정연도_공휴일_가져오기() {
        Integer year = 2025;
        String countryCode = "US";
        RestClient restClient = RestClient.builder()
                .messageConverters(converters -> {
                    converters.add(new MappingJackson2HttpMessageConverter() {{
                        setSupportedMediaTypes(List.of(
                                MediaType.APPLICATION_JSON,
                                new MediaType("text", "json", StandardCharsets.UTF_8)
                        ));
                    }});
                })
                .build();
        List<HolidayInfoDto> result = restClient.get()
                .uri(publicHolidayApi + "/{year}/{countryCode}", year, countryCode)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        Assertions.assertNotNull(result);
        for (HolidayInfoDto dto : result) {
            System.out.println(dto);
        }
    }
}