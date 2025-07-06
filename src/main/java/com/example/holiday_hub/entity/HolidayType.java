package com.example.holiday_hub.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum HolidayType {
    PUBLIC("Public"),
    BANK("Bank"),
    SCHOOL("School"),
    AUTHORITIES("Authorities"),
    OPTIONAL("Optional"),
    OBSERVANCE("Observance");

    private final String name;

    @JsonCreator
    public static HolidayType from(String value) {
        return Arrays.stream(HolidayType.values())
                .filter(type -> type.name.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알려지지 않은 타입: " + value));
    }
}
