package com.example.holiday_hub.controller;

import com.example.holiday_hub.dto.HolidayInfoDto;
import com.example.holiday_hub.dto.HolidaySearchCondition;
import com.example.holiday_hub.entity.HolidayType;
import com.example.holiday_hub.service.HolidayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "공휴일 API", description = "공휴일 관련 조회, 삭제, 덮어쓰기 관련 API")

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/holidays")
public class HolidayController {
    private final HolidayService holidayService;

    @Operation(summary = "공휴일 정보 조회", description = "시작 날짜, 종료 날짜, 국가 코드, 공휴일 타입 등을 넣어 조건에 맞는 공휴일 결과 반환. 모든 파라미터는 생략 가능")
    @Parameter(
            name = "from",
            description = "시작 날짜, null 가능",
            example = "2024-01-01"
    )
    @Parameter(
            name = "to",
            description = "종료 날짜, null 가능",
            example = "2025-01-01"
    )
    @Parameter(
            name = "countryCode",
            description = "국가 코드, null 가능| ex) KR",
            example = "KR"
    )
    @Parameter(
            name = "type",
            description = "공휴일 타입. [PUBLIC, BANK, SCHOOL, AUTHORITIES, OPTIONAL, OBSERVANCE]",
            example = "PUBLIC"
    )
    @GetMapping
    public Page<HolidayInfoDto> searchHolidays(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) String type,
            Pageable pageable
    ) {
        HolidayType holidayType = null;
        if(type != null) {
            holidayType = HolidayType.from(type);
        }
        return holidayService.search(new HolidaySearchCondition(from, to, countryCode, holidayType, pageable));
    }

    @Operation(summary = "공휴일 정보 덮어쓰기", description = "년도, 국가 코드를 넣어 특정년도, 국가코드에 해당하는 공휴일 데이터 덮어쓰기")
    @Parameter(
            name = "year",
            description = "특정연도",
            example = "2024"
    )
    @Parameter(
            name = "countryCode",
            description = "국가 코드",
            example = "KR"
    )
    @PutMapping("/sync")
    public ResponseEntity<Void> updateHoliday(@RequestParam int year, @RequestParam String countryCode) {
        holidayService.updateHoliday(year, countryCode);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "공휴일 정보 지우기", description = "년도, 국가 코드를 넣어 특정년도, 국가코드에 해당하는 공휴일 데이터 지우기")
    @Parameter(
            name = "year",
            description = "특정연도",
            example = "2024"
    )
    @Parameter(
            name = "countryCode",
            description = "국가 코드",
            example = "KR"
    )
    @DeleteMapping
    public ResponseEntity<Void> deleteHoliday(@RequestParam int year, @RequestParam String countryCode) {
        holidayService.deleteHoliday(year, countryCode);
        return ResponseEntity.ok().build();
    }

}
