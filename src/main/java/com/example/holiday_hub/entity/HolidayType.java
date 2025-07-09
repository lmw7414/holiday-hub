package com.example.holiday_hub.entity;

import com.example.holiday_hub.exception.ErrorCode;
import com.example.holiday_hub.exception.HolidayApplicationException;
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
                .filter(type -> value.equalsIgnoreCase(type.name))
                .findFirst()
                .orElseThrow(() -> new HolidayApplicationException(ErrorCode.UNSUPPORTED_HOLIDAY_TYPE));
    }
}
