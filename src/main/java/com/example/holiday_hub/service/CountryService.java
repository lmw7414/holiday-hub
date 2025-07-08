package com.example.holiday_hub.service;

import com.example.holiday_hub.dto.CountryInfoDto;
import com.example.holiday_hub.entity.CountryInfo;
import com.example.holiday_hub.exception.ErrorCode;
import com.example.holiday_hub.exception.HolidayApplicationException;
import com.example.holiday_hub.repository.CountryInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryInfoRepository countryInfoRepository;

    @Transactional(readOnly = true)
    public List<CountryInfoDto> findAll() {
        return countryInfoRepository.findAll().stream().map(CountryInfoDto::from).toList();
    }

    @Transactional(readOnly = true)
    public CountryInfo getCountryInfoOrException(String countryCode) {
        return countryInfoRepository.findById(countryCode)
                .orElseThrow(() -> new HolidayApplicationException(ErrorCode.COUNTRY_CODE_NOT_FOUND));
    }
}
