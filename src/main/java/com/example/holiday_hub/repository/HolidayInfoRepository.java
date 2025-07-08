package com.example.holiday_hub.repository;

import com.example.holiday_hub.entity.HolidayInfo;
import com.example.holiday_hub.repository.querydsl.HolidayInfoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface HolidayInfoRepository extends JpaRepository<HolidayInfo, Long>, HolidayInfoRepositoryCustom {

    @Modifying
    @Query("DELETE FROM HolidayInfo h WHERE h.date BETWEEN :start AND :end AND h.countryCode = :countryCode")
    void deleteAllByYearAndCountryCode(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            @Param("countryCode") String countryCode
    );

    HolidayInfo findByDateAndCountryCodeAndName(LocalDate date, String countryCode, String name);
}
