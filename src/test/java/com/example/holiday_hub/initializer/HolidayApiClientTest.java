package com.example.holiday_hub.initializer;

import com.example.holiday_hub.dto.CountryInfoDto;
import com.example.holiday_hub.dto.HolidayInfoDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class HolidayApiClientTest {
    @Autowired
    private HolidayApiClient client;

    @Test
    void 나라_정보를_호출했을때_정상적인_데이터를_반환한다() {
        // When
        List<CountryInfoDto> result = client.fetchCountries();
        // Then
        assertNotNull(result);
        for (CountryInfoDto dto : result) {
            System.out.println(dto);
            assertThat(dto.countryCode()).isNotBlank();
            assertThat(dto.name()).isNotBlank();
        }
    }

    @ParameterizedTest(name = "{index}-{argumentsWithNames}")
    @MethodSource("countryAndYear")
    void 나라코드와_년도가_주어졌을때_정상적인_데이터를_반환한다(String countryCode, int year) {
        // When
        List<HolidayInfoDto> result = client.fetchHolidays(year, countryCode);
        // Then
        assertNotNull(result);
        for (HolidayInfoDto dto : result) {
            System.out.println(dto);
            assertNotNull(dto.date(), "날짜는 null일 수 없음");
            assertNotNull(dto.localName(), "현지명은 null일 수 없음");
            assertNotNull(dto.name(), "영어 이름은 null일 수 없음");
            assertEquals(countryCode, dto.countryCode(), "나라코드는 요청한 값과 동일해야 함");
        }
    }

    static Stream<Arguments> countryAndYear() {
        return Stream.of(
                Arguments.of("KR", "2020"),
                Arguments.of("IT", "2021"),
                Arguments.of("US", "2022"),
                Arguments.of("MG", "2023"),
                Arguments.of("LI", "2025")
        );
    }
}
