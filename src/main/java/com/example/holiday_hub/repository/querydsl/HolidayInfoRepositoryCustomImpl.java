package com.example.holiday_hub.repository.querydsl;

import com.example.holiday_hub.dto.HolidaySearchCondition;
import com.example.holiday_hub.entity.HolidayInfo;
import com.example.holiday_hub.entity.HolidayType;
import com.example.holiday_hub.entity.QHolidayInfo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HolidayInfoRepositoryCustomImpl implements HolidayInfoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<HolidayInfo> findByDynamicConditions(HolidaySearchCondition condition) {
        QHolidayInfo holidayInfo = QHolidayInfo.holidayInfo;
        List<HolidayInfo> result = queryFactory.selectFrom(holidayInfo)
                .where(buildWhereCondition(holidayInfo, condition))
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize())
                .orderBy(holidayInfo.date.asc())
                .fetch();
        long total = Optional.ofNullable(
                        queryFactory.select(holidayInfo.count())
                                .from(holidayInfo)
                                .where(buildWhereCondition(holidayInfo, condition))
                                .fetchOne()
                )
                .orElse(0L);
        return new PageImpl<>(result, condition.pageable(), total);
    }

    private BooleanExpression[] buildWhereCondition(QHolidayInfo holidayInfo, HolidaySearchCondition condition) {
        return new BooleanExpression[]{
                betweenFromTo(holidayInfo, condition.from(), condition.to()),
                eqCountryCode(holidayInfo, condition.countryCode()),
                eqType(holidayInfo, condition.type())
        };
    }

    private BooleanExpression betweenFromTo(QHolidayInfo holidayInfo, LocalDate from, LocalDate to) {
        if (from == null && to == null) return null;
        if (from != null && to != null) return holidayInfo.date.between(from, to);

        if (from != null) return holidayInfo.date.goe(from);
        else return holidayInfo.date.loe(to);
    }

    private BooleanExpression eqCountryCode(QHolidayInfo holidayInfo, String code) {
        if (code == null) return null;
        return holidayInfo.countryCode.eq(code);
    }

    private BooleanExpression eqType(QHolidayInfo holidayInfo, HolidayType holidayType) {
        if (holidayType == null) return null;
        return holidayInfo.types.containsIgnoreCase(holidayType.name());
    }
}
