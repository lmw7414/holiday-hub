package com.example.holiday_hub.entity.converter;

import com.example.holiday_hub.entity.HolidayType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class HolidayTypeListConverter implements AttributeConverter<List<HolidayType>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<HolidayType> attribute) {
        if (attribute == null) return null;
        return attribute.stream().map(HolidayType::name).sorted().collect(Collectors.joining(DELIMITER));
    }

    @Override
    public List<HolidayType> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) return List.of();
        return Arrays.stream(dbData.split(DELIMITER)).map(HolidayType::valueOf).toList();
    }
}
