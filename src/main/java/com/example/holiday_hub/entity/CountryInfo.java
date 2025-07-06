package com.example.holiday_hub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table
@Entity
public class CountryInfo {

    @Id
    private String countryCode;
    private String name;

    protected CountryInfo(){}

    private CountryInfo(String countryCode, String name) {
        this.countryCode = countryCode;
        this.name = name;
    }

    public static CountryInfo of(String countryCode, String name) {
        return new CountryInfo(countryCode, name);
    }
}
