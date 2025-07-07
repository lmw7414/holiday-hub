package com.example.holiday_hub.repository;

import com.example.holiday_hub.entity.HolidayInfo;
import com.example.holiday_hub.repository.querydsl.HolidayInfoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayInfoRepository extends JpaRepository<HolidayInfo, Long>, HolidayInfoRepositoryCustom {
}
