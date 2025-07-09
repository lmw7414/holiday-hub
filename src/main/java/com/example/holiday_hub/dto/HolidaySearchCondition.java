package com.example.holiday_hub.dto;

import com.example.holiday_hub.entity.HolidayType;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public record HolidaySearchCondition(
        LocalDate from,
        LocalDate to,
        String countryCode,
        HolidayType type,
        Pageable pageable
) {}