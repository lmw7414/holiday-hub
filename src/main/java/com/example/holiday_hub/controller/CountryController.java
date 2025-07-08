package com.example.holiday_hub.controller;

import com.example.holiday_hub.dto.CountryInfoDto;
import com.example.holiday_hub.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/countries")
public class CountryController {
    private final CountryService countryService;

    @GetMapping
    public ResponseEntity<List<CountryInfoDto>> findAll() {
        return ResponseEntity.ok().body(countryService.findAll());
    }
}
