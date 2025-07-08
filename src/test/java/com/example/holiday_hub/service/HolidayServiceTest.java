package com.example.holiday_hub.service;

import com.example.holiday_hub.dto.HolidayInfoDto;
import com.example.holiday_hub.dto.HolidaySearchCondition;
import com.example.holiday_hub.entity.HolidayInfo;
import com.example.holiday_hub.entity.HolidayType;
import com.example.holiday_hub.exception.ErrorCode;
import com.example.holiday_hub.exception.HolidayApplicationException;
import com.example.holiday_hub.initializer.HolidayApiClient;
import com.example.holiday_hub.initializer.HolidaySyncInitializer;
import com.example.holiday_hub.repository.HolidayInfoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class HolidayServiceTest {

    @Autowired
    private HolidayService sut;

    @MockitoBean
    private HolidaySyncInitializer syncInitializer;
    @MockitoBean
    private HolidayApiClient holidayApiClient;
    @MockitoBean
    private HolidayInfoRepository holidayInfoRepository;
    @MockitoBean
    private CountryService countryService;


    @Test
    void 지원하지_않는_년도의_데이터가_넘어왔을_때_에러반환() {
        // Given
        int year = 2010;
        HolidaySearchCondition condition = new HolidaySearchCondition(
                LocalDate.of(year, 1, 1),
                null,
                "KR",
                null,
                Pageable.ofSize(10)
        );
        // When & Then
        HolidayApplicationException ex = assertThrows(HolidayApplicationException.class, () -> sut.search(condition));
        assertEquals(ErrorCode.UNSUPPORTED_YEAR_RANGE, ex.getErrorCode());
    }

    @Test
    void 잘못된_날짜_범위_데이터가_넘어왔을_때_에러반환() {
        // Given
        LocalDate from = LocalDate.of(2025, 1, 1);
        LocalDate to = LocalDate.of(2024, 1, 1);
        HolidaySearchCondition condition = new HolidaySearchCondition(
                from,
                to,
                "KR",
                null,
                Pageable.ofSize(10)
        );
        // When & Then
        HolidayApplicationException ex = assertThrows(HolidayApplicationException.class, () -> sut.search(condition));
        assertEquals(ErrorCode.INVALID_DATE_RANGE, ex.getErrorCode());
    }

    @Test
    void 존재하지_않는_국가코드_데이터가_넘어왔을_때_에러반환() {
        // Given
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2025, 1, 1);
        String countryCode = "TEST";
        HolidaySearchCondition condition = new HolidaySearchCondition(
                from,
                to,
                countryCode,
                null,
                Pageable.ofSize(10)
        );
        when(countryService.getCountryInfoOrException(countryCode)).thenThrow(new HolidayApplicationException(ErrorCode.COUNTRY_CODE_NOT_FOUND));
        // When & Then
        HolidayApplicationException ex = assertThrows(HolidayApplicationException.class, () -> sut.search(condition));
        assertEquals(ErrorCode.COUNTRY_CODE_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void 덮어쓰기_이미_존재하는_공휴일_정보가_있다면_업데이트() {
        // Given
        int year = 2024;
        String countryCode = "KR";
        HolidayInfo exist = HolidayInfo.of(
                LocalDate.of(year,1,1),
                "신정",
                "New Year's Day",
                countryCode,
                true,
                true,
                null,
                null,
                List.of(HolidayType.PUBLIC)
        );
        HolidayInfoDto newDto = new HolidayInfoDto(LocalDate.of(2024, 1, 1),
                "TEST-신정",
                "New Year's Day",
                countryCode,
                Boolean.FALSE,
                Boolean.FALSE,
                null,
                null,
                List.of(HolidayType.PUBLIC, HolidayType.OPTIONAL)
        );

        when(holidayApiClient.fetchHolidays(year, countryCode)).thenReturn(List.of(newDto));
        when(holidayInfoRepository.findByDateAndCountryCodeAndName(newDto.date(), countryCode, newDto.name())).thenReturn(exist);

        // When
        sut.updateHoliday(year, countryCode);

        // Then
        verify(countryService, times(1)).getCountryInfoOrException(countryCode);
        verify(holidayInfoRepository, times(1)).saveAll(Collections.emptyList());
        assertThat(exist.getLocalName()).isEqualTo("TEST-신정");
        assertThat(exist.isFixed()).isFalse();
        assertThat(exist.isGlobal()).isFalse();
        assertThat(exist.getTypes()).containsExactlyInAnyOrder(HolidayType.PUBLIC, HolidayType.OPTIONAL);
    }

    @Test
    void 덮어쓰기__존재하지_않는_공휴일_정보가_있다면_새로_저장() {
        // Given
        int year = 2024;
        String countryCode = "KR";

        HolidayInfoDto newDto = new HolidayInfoDto(LocalDate.of(2024, 1, 1),
                "TEST-신정",
                "New Year's Day",
                countryCode,
                Boolean.FALSE,
                Boolean.FALSE,
                null,
                null,
                List.of(HolidayType.PUBLIC, HolidayType.OPTIONAL)
        );

        when(holidayApiClient.fetchHolidays(year, countryCode)).thenReturn(List.of(newDto));
        when(holidayInfoRepository.findByDateAndCountryCodeAndName(newDto.date(), countryCode, newDto.name())).thenReturn(null);

        // When
        sut.updateHoliday(year, countryCode);

        // Then
        ArgumentCaptor<List<HolidayInfo>> captor = ArgumentCaptor.forClass(List.class);
        verify(countryService, times(1)).getCountryInfoOrException(countryCode);
        verify(holidayInfoRepository, times(1)).saveAll(captor.capture());
        List<HolidayInfo> saved = captor.getValue();
        assertThat(saved).hasSize(1);
        assertThat(saved.get(0).getName()).isEqualTo("New Year's Day");
        assertThat(saved.get(0).getTypes()).containsExactlyInAnyOrder(HolidayType.PUBLIC, HolidayType.OPTIONAL);
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