package com.example.holiday_hub.service;

import com.example.holiday_hub.dto.HolidayInfoDto;
import com.example.holiday_hub.dto.HolidaySearchCondition;
import com.example.holiday_hub.initializer.HolidayApiClient;
import com.example.holiday_hub.repository.HolidayInfoRepository;
import lombok.RequiredArgsConstructor;
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

    // 필터 기반 데이터 조회하기
    @Transactional(readOnly = true)
    public Page<HolidayInfoDto> search(HolidaySearchCondition condition) {
        return holidayInfoRepository.findByDynamicConditions(condition).map(HolidayInfoDto::from);
    }
    // 특정 연도 국가로 덮어쓰기
    @Transactional
    public void updateHoliday(int year, String countryCode) {
        deleteHoliday(year, countryCode);
        List<HolidayInfoDto> updated = client.fetchHolidays(year, countryCode);
        holidayInfoRepository.saveAll(updated.stream().map(HolidayInfoDto::toEntity).toList());
    }


    // 특정 연도, 국가의 공휴일 레코드 전체 삭제
    @Transactional
    public void deleteHoliday(int year, String countryCode) {
        holidayInfoRepository.deleteAllByYearAndCountryCode(LocalDate.of(year,1,1), LocalDate.of(year, 12, 31), countryCode);
    }

}
