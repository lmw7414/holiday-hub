package com.example.holiday_hub.service;

import com.example.holiday_hub.dto.HolidayInfoDto;
import com.example.holiday_hub.dto.HolidaySearchCondition;
import com.example.holiday_hub.exception.ErrorCode;
import com.example.holiday_hub.exception.HolidayApplicationException;
import com.example.holiday_hub.initializer.HolidayApiClient;
import com.example.holiday_hub.repository.HolidayInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayService {
    private final HolidayApiClient client;
    private final HolidayInfoRepository holidayInfoRepository;
    private final CountryService countryService;

    @Value("${support.year.from}")
    private Integer fromYear;
    @Value("${support.year.to}")
    private Integer toYear;

    // 필터 기반 데이터 조회하기
    @Transactional(readOnly = true)
    public Page<HolidayInfoDto> search(HolidaySearchCondition condition) {
        validateSearchCondition(condition);
        return holidayInfoRepository.findByDynamicConditions(condition).map(HolidayInfoDto::from);
    }

    // 특정 연도 국가로 덮어쓰기
    @Transactional
    public void updateHoliday(int year, String countryCode) {
        validateYear(year);
        countryService.getCountryInfoOrException(countryCode);
        deleteHoliday(year, countryCode);
        List<HolidayInfoDto> updated = client.fetchHolidays(year, countryCode);
        holidayInfoRepository.saveAll(updated.stream().map(HolidayInfoDto::toEntity).toList());
    }


    // 특정 연도, 국가의 공휴일 레코드 전체 삭제
    @Transactional
    public void deleteHoliday(int year, String countryCode) {
        validateYear(year);
        countryService.getCountryInfoOrException(countryCode);
        countryService.getCountryInfoOrException(countryCode);
        holidayInfoRepository.deleteAllByYearAndCountryCode(LocalDate.of(year, 1, 1), LocalDate.of(year, 12, 31), countryCode);
    }

    private void validateSearchCondition(HolidaySearchCondition condition) {
        if (condition.from() != null && condition.to() != null) {
            if (condition.from().isAfter(condition.to())) {
                throw new HolidayApplicationException(ErrorCode.INVALID_DATE_RANGE);
            }
        }
        if (condition.from() != null) {
            validateYear(condition.from().getYear());
        }
        if (condition.to() != null) {
            validateYear(condition.to().getYear());
        }
        if (condition.countryCode() != null) {
            countryService.getCountryInfoOrException(condition.countryCode());
        }

    }

    private void validateYear(int year) {
        if (year < fromYear || year > toYear) {
            throw new HolidayApplicationException(ErrorCode.UNSUPPORTED_YEAR_RANGE);
        }
    }

}
