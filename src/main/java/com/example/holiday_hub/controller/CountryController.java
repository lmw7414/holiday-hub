package com.example.holiday_hub.controller;

import com.example.holiday_hub.dto.CountryInfoDto;
import com.example.holiday_hub.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "국가 API", description = "국가 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/countries")
public class CountryController {
    private final CountryService countryService;

    @Operation(summary = "국가 정보 전체 조회", description = "조회 요청 시 국가 이름, 국가 코드 리스트 반환")
    @GetMapping
    public ResponseEntity<List<CountryInfoDto>> findAll() {
        return ResponseEntity.ok().body(countryService.findAll());
    }
}
