package com.example.holiday_hub.dto;

import com.example.holiday_hub.entity.HolidayInfo;
import com.example.holiday_hub.entity.HolidayType;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public record HolidayInfoDto(
        LocalDate date,
        String localName,
        String name,
        String countryCode,
        Boolean fixed,
        Boolean global,
        List<String> counties,
        Integer launchYear,
        List<HolidayType> types
) {
    public static HolidayInfoDto from(HolidayInfo entity) {
        return new HolidayInfoDto(
                entity.getDate(),
                entity.getLocalName(),
                entity.getName(),
                entity.getCountryCode(),
                entity.isFixed(),
                entity.isGlobal(),
                Arrays.stream(entity.getCounties().split(",")).toList(),
                entity.getLaunchYear(),
                entity.getTypes()
        );
    }

    public static HolidayInfo toEntity(HolidayInfoDto dto) {
        String countiesToString = null;
        Integer launchYear = null;
        if(dto.counties != null) countiesToString = dto.counties.stream().sorted().collect(Collectors.joining(","));
        if(dto.launchYear != null) launchYear = dto.launchYear;
        return HolidayInfo.of(
                dto.date,
                dto.localName,
                dto.name,
                dto.countryCode,
                dto.fixed,
                dto.global,
                countiesToString,
                launchYear,
                dto.types
                );
    }
}
