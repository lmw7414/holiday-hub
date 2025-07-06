package com.example.holiday_hub.repository;

import com.example.holiday_hub.entity.CountryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryInfoRepository extends JpaRepository<CountryInfo, String> {
}
