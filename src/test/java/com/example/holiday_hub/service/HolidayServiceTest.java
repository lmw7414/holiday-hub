package com.example.holiday_hub.service;

import com.example.holiday_hub.dto.HolidayInfoDto;
import com.example.holiday_hub.entity.HolidayInfo;
import com.example.holiday_hub.entity.HolidayType;
import com.example.holiday_hub.initializer.HolidayApiClient;
import com.example.holiday_hub.repository.HolidayInfoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class HolidayServiceTest {
    private final HolidayApiClient holidayApiClient = mock(HolidayApiClient.class);
    private final HolidayInfoRepository holidayInfoRepository = mock(HolidayInfoRepository.class);
    private final  HolidayService sut = new HolidayService(holidayApiClient, holidayInfoRepository);

    // 특정 연도 국가로 덮어쓰기
    @Test
    void 특정년도와_나라코드가_주어졌을때_정상동작() {
        // Given
        int year = 2024;
        String countryCode = "KR";
        when(holidayApiClient.fetchHolidays(year, countryCode))
                .thenReturn(List.of(
                        new HolidayInfoDto(LocalDate.of(2024, 1, 1),
                                "TEST-localName",
                                "TEST-name",
                                countryCode,
                                Boolean.FALSE,
                                Boolean.FALSE,
                                null,
                                null,
                                List.of(HolidayType.PUBLIC)
                        )
        ));
        ArgumentCaptor<List<HolidayInfo>> captor = ArgumentCaptor.forClass(List.class);
        // When
        sut.updateHoliday(year, countryCode);
        // Then
        verify(holidayInfoRepository, times(1)).deleteAllByYearAndCountryCode(any(LocalDate.class), any(LocalDate.class), eq(countryCode));
        verify(holidayInfoRepository, times(1)).saveAll(captor.capture());
        assertThat(captor.getValue()).hasSize(1);
    }

    @Test
    void 년도와_국가코드가_주어졌을때_삭제_정상동작() {
        // Given
        int year = 2025;
        String countryCode = "KR";
        // When
        sut.deleteHoliday(year, countryCode);
        // Then
        verify(holidayInfoRepository, times(1))
                .deleteAllByYearAndCountryCode(
                        eq(LocalDate.of(2025, 1, 1)),
                        eq(LocalDate.of(2025, 12, 31)),
                        eq("KR")
                );
    }

}