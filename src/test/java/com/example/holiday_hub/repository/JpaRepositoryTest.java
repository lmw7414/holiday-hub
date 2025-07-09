package com.example.holiday_hub.repository;

import com.example.holiday_hub.configuration.QueryDslConfig;
import com.example.holiday_hub.dto.HolidaySearchCondition;
import com.example.holiday_hub.entity.HolidayInfo;
import com.example.holiday_hub.entity.HolidayType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import(QueryDslConfig.class)
class JpaRepositoryTest {
    @Autowired
    HolidayInfoRepository holidayInfoRepository;

    @BeforeEach
    void setup() {
        holidayInfoRepository.saveAll(
                List.of(
                        HolidayInfo.of(LocalDate.of(2024, 1, 1), "새해", "New Year's Day", "KR", Boolean.FALSE, Boolean.TRUE, null, null, List.of(HolidayType.PUBLIC)),
                        HolidayInfo.of(LocalDate.of(2025, 1, 1), "새해", "New Year's Day", "KR", Boolean.FALSE, Boolean.TRUE, null, null, List.of(HolidayType.PUBLIC)),
                        HolidayInfo.of(LocalDate.of(2024, 3, 1), "3·1절", "Independence Movement Day", "KR", Boolean.FALSE, Boolean.TRUE, null, null, List.of(HolidayType.PUBLIC)),
                        HolidayInfo.of(LocalDate.of(2024, 12, 25), "크리스마스", "Christmas Day", "KR", Boolean.FALSE, Boolean.TRUE, null, null, List.of(HolidayType.PUBLIC)),
                        HolidayInfo.of(LocalDate.of(2024, 12, 31), "마지막 날", "Last Day", "KR", Boolean.FALSE, Boolean.TRUE, null, null, List.of(HolidayType.OPTIONAL)),
                        HolidayInfo.of(LocalDate.of(2024, 1, 15), "Martin Luther King, Jr. Day", "Martin Luther King, Jr. Day", "US", Boolean.FALSE, Boolean.TRUE, null, null, List.of(HolidayType.PUBLIC)),
                        HolidayInfo.of(LocalDate.of(2024, 2, 12), "Lincoln's Birthday", "Lincoln's Birthday", "US", Boolean.FALSE, Boolean.FALSE, "US-CA,US-CT,US-IL,US-IN,US-KY,US-MI,US-MO,US-NY,US-OH", null, List.of(HolidayType.OBSERVANCE)),
                        HolidayInfo.of(LocalDate.of(2025, 11, 8), "TEST-1", "TEST-1", "TS1", Boolean.FALSE, Boolean.FALSE, "TS1-TT, TS1-TK, TS1-TR", null, List.of(HolidayType.AUTHORITIES, HolidayType.SCHOOL)),
                        HolidayInfo.of(LocalDate.of(2025, 2, 8), "TEST-2", "TEST-2", "TS1", Boolean.FALSE, Boolean.FALSE, null, null, List.of(HolidayType.SCHOOL)),
                        HolidayInfo.of(LocalDate.of(2025, 3, 8), "TEST-3", "TEST-3", "TS2", Boolean.FALSE, Boolean.FALSE, "TS2-KR", null, List.of(HolidayType.PUBLIC)),
                        HolidayInfo.of(LocalDate.of(2025, 1, 8), "TEST-4", "TEST-4", "TS2", Boolean.FALSE, Boolean.FALSE, null, null, List.of(HolidayType.OPTIONAL)),
                        HolidayInfo.of(LocalDate.of(2025, 2, 26), "TEST-5", "TEST-5", "TS1", Boolean.FALSE, Boolean.FALSE, null, null, List.of(HolidayType.BANK, HolidayType.OBSERVANCE)),
                        HolidayInfo.of(LocalDate.of(2025, 5, 30), "TEST-6", "TEST-6", "TS3", Boolean.FALSE, Boolean.FALSE, null, null, List.of(HolidayType.OBSERVANCE)),
                        HolidayInfo.of(LocalDate.of(2026, 10, 11), "TEST-7", "TEST-7", "TS3", Boolean.FALSE, Boolean.FALSE, null, null, List.of(HolidayType.AUTHORITIES)),
                        HolidayInfo.of(LocalDate.of(2026, 1, 1), "TEST-8", "TEST-8", "TS4", Boolean.FALSE, Boolean.FALSE, null, null, List.of(HolidayType.AUTHORITIES, HolidayType.PUBLIC)),
                        HolidayInfo.of(LocalDate.of(2027, 4, 9), "TEST-9", "TEST-9", "TS5", Boolean.FALSE, Boolean.FALSE, null, null, List.of(HolidayType.OPTIONAL)),
                        HolidayInfo.of(LocalDate.of(2027, 3, 12), "TEST-10", "TEST-10", "TS7", Boolean.FALSE, Boolean.FALSE, null, null, List.of(HolidayType.SCHOOL))
                )
        );
    }

    @Test
    void 아무런_조건없이_조회_시_공휴일_페이지_반환_날짜순_정렬() {
        // Given
        Pageable pageable = Pageable.ofSize(20);
        HolidaySearchCondition condition = new HolidaySearchCondition(null, null, null, null, pageable);
        // When
        Page<HolidayInfo> result = holidayInfoRepository.findByDynamicConditions(condition);
        // Then
        assertThat(result).isNotNull().hasSize((int) holidayInfoRepository.count());
        for (HolidayInfo info : result) System.out.println(info);
    }

    @Test
    void 기간이_주어졌을때_해당_기간의_공휴일_페이지_반환() {
        // Given
        int pageSize = 10;
        Pageable pageable = Pageable.ofSize(pageSize);
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 5, 1);
        HolidaySearchCondition condition = new HolidaySearchCondition(start, end, null, null, pageable);
        // When
        Page<HolidayInfo> result = holidayInfoRepository.findByDynamicConditions(condition);
        // Then
        assertThat(result.getContent().size()).isLessThanOrEqualTo(pageSize);
        // 기간 이내의 값들만 있는지
        assertThat(result.getContent().stream()
                .allMatch(holiday -> !holiday.getDate().isBefore(start) && !holiday.getDate().isAfter(end))).isTrue();
    }

    @Test
    void 시작하는_기간만_주어졌을때_이후_기간의_공휴일_페이지_반환() {
        // Given
        int pageSize = 10;
        Pageable pageable = Pageable.ofSize(pageSize);
        LocalDate start = LocalDate.of(2026, 1, 1);
        HolidaySearchCondition condition = new HolidaySearchCondition(start, null, null, null, pageable);
        // When
        Page<HolidayInfo> result = holidayInfoRepository.findByDynamicConditions(condition);
        // Then
        assertThat(result.getContent().size()).isLessThanOrEqualTo(pageSize);
        // 기간 이내의 값들만 있는지
        assertThat(result.getContent().stream()
                .noneMatch(holiday -> holiday.getDate().isBefore(start))).isTrue(); // start 이전 날짜가 없어야 함
    }

    @Test
    void 국가코드만_주어졌을때_해당_국가의_공휴일_페이지_반환() {
        // Given
        int pageSize = 10;
        Pageable pageable = Pageable.ofSize(pageSize);
        String countryCode = "TS1";
        HolidaySearchCondition condition = new HolidaySearchCondition(null, null, countryCode, null, pageable);
        // When
        Page<HolidayInfo> result = holidayInfoRepository.findByDynamicConditions(condition);
        // Then
        assertThat(result.getContent().size()).isLessThanOrEqualTo(pageSize);
        //국가 코드가 동일한지
        assertThat(result.getContent().stream()
                .allMatch(holiday -> countryCode.equals(holiday.getCountryCode()))
        ).isTrue();
    }

    @Test
    void 기간과_국가코드가_주어졌을때_해당_국가의_기간_내_공휴일_페이지_반환() {
        // Given
        int pageSize = 10;
        Pageable pageable = Pageable.ofSize(pageSize);
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 5, 1);
        String countryCode = "KR";
        HolidaySearchCondition condition = new HolidaySearchCondition(start, end, countryCode, null, pageable);
        // When
        Page<HolidayInfo> result = holidayInfoRepository.findByDynamicConditions(condition);
        // Then
        assertThat(result.getContent().size()).isLessThanOrEqualTo(pageSize);
        // 기간 이내의 값들만 있는지, 국가 코드는 동일한지
        assertThat(result.getContent().stream()
                .allMatch(holiday -> !holiday.getDate().isBefore(start) &&
                        !holiday.getDate().isAfter(end) &&
                        countryCode.equals(holiday.getCountryCode()))
        ).isTrue();
    }

    @ParameterizedTest
    @MethodSource("holidayType")
    void 공휴일_타입이_주어졌을때_해당_타입의_공휴일_페이지_반환(HolidayType type) {
        // Given
        int pageSize = 10;
        Pageable pageable = Pageable.ofSize(pageSize);
        HolidaySearchCondition condition = new HolidaySearchCondition(null, null, null, type, pageable);
        // When
        Page<HolidayInfo> result = holidayInfoRepository.findByDynamicConditions(condition);
        // Then
        assertThat(result.getContent().size()).isLessThanOrEqualTo(pageSize);
        //타입이 동일한지
        assertThat(result.getContent().stream()
                .allMatch(holiday -> holiday.getTypes().contains(type))
        ).isTrue();
    }

    @Test
    void 기간과_나라코드가_주어졌을때_범위에_해당하는_모든_데이터를_삭제한다() {
        // Given
        int year = 2024;
        String countryCode = "KR";
        LocalDate from = LocalDate.of(year, 1, 1);
        LocalDate to = LocalDate.of(year, 12, 31);
        long beforeCount = holidayInfoRepository.count();
        long afterCount = holidayInfoRepository.findAll().stream()
                .filter(holidayInfo -> holidayInfo.getCountryCode().equals(countryCode) &&
                        !holidayInfo.getDate().isBefore(from) && !holidayInfo.getDate().isAfter(to)
                )
                .count();
        // When
        holidayInfoRepository.deleteAllByYearAndCountryCode(from, to, countryCode);
        // Then
        assertEquals(beforeCount - afterCount, holidayInfoRepository.count());
    }

    static Stream<Arguments> holidayType() {
        return Stream.of(
                Arguments.of(HolidayType.PUBLIC),
                Arguments.of(HolidayType.BANK),
                Arguments.of(HolidayType.OBSERVANCE),
                Arguments.of(HolidayType.SCHOOL),
                Arguments.of(HolidayType.AUTHORITIES)
        );
    }
}