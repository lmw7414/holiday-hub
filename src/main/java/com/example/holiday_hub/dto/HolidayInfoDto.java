package com.example.holiday_hub.dto;


import com.example.holiday_hub.entity.HolidayType;

import java.time.LocalDate;
import java.util.List;

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
}
