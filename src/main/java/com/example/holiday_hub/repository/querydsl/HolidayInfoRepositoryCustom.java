package com.example.holiday_hub.repository.querydsl;

import com.example.holiday_hub.dto.HolidaySearchCondition;
import com.example.holiday_hub.entity.HolidayInfo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayInfoRepositoryCustom {
    Page<HolidayInfo> findByDynamicConditions(HolidaySearchCondition condition);
}
