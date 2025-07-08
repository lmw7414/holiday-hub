package com.example.holiday_hub.controller;

import com.example.holiday_hub.dto.HolidayInfoDto;
import com.example.holiday_hub.dto.HolidaySearchCondition;
import com.example.holiday_hub.entity.HolidayType;
import com.example.holiday_hub.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/holidays")
public class HolidayController {
    private final HolidayService holidayService;

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

    @PutMapping("/sync")
    public ResponseEntity<Void> updateHoliday(@RequestParam int year, @RequestParam String countryCode) {
        holidayService.updateHoliday(year, countryCode);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteHoliday(@RequestParam int year, @RequestParam String countryCode) {
        holidayService.deleteHoliday(year, countryCode);
        return ResponseEntity.ok().build();
    }


}
