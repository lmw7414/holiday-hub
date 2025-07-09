package com.example.holiday_hub.dto;

import com.example.holiday_hub.entity.CountryInfo;

public record CountryInfoDto(
        String countryCode,
        String name
) {
    public static CountryInfo toEntity(CountryInfoDto dto) {
        return CountryInfo.of(dto.countryCode, dto.name);
    }

    public static CountryInfoDto from(CountryInfo entity) {
        return new CountryInfoDto(entity.getCountryCode(), entity.getName());
    }
}
