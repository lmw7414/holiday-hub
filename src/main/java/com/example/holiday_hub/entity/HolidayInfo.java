package com.example.holiday_hub.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Table
@Entity
@Getter
public class HolidayInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private LocalDate date;
    private String localName;
    private String name;
    private String countryCode;
    private boolean fixed;
    private boolean global;
    private String counties;
    private Integer launchYear;
    private String types;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;

    protected HolidayInfo() {
    }

    private HolidayInfo(LocalDate date, String localName, String name, String countryCode, Boolean fixed, Boolean global, String counties, Integer launchYear, List<HolidayType> types) {
        this.date = date;
        this.localName = localName;
        this.name = name;
        this.countryCode = countryCode;
        this.fixed = fixed;
        this.global = global;
        this.counties = counties;
        this.launchYear = launchYear;
        this.types = (types == null) ? null : types.stream().map(HolidayType::name).sorted().collect(Collectors.joining(","));
    }

    public static HolidayInfo of(LocalDate date, String localName, String name, String countryCode, Boolean fixed, Boolean global, String counties, Integer launchYear, List<HolidayType> types) {
        return new HolidayInfo(date, localName, name, countryCode, fixed, global, counties, launchYear, types);
    }

    @PrePersist
    void registeredAt() {
        this.registeredAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public List<HolidayType> getTypes() {
        if (types == null || types.isBlank()) return List.of();
        return Arrays.stream(types.split(",")).map(HolidayType::valueOf).toList();
    }

    public void update(String localName, boolean fixed, boolean global, Integer launchYear, List<HolidayType> types, String counties) {
        this.localName = localName;
        this.fixed = fixed;
        this.global = global;
        this.launchYear = launchYear;
        this.types = (types == null) ? null : types.stream().map(HolidayType::name).sorted().collect(Collectors.joining(","));
        this.counties = counties;
        this.updatedAt = LocalDateTime.now();
    }

}
