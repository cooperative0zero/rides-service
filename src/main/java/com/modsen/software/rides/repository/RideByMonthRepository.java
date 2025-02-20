package com.modsen.software.rides.repository;

import com.modsen.software.rides.entity.RideByMonth;
import com.modsen.software.rides.entity.enumeration.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RideByMonthRepository extends JpaRepository<RideByMonth, Long> {
    Optional<RideByMonth> findByDriverIdAndMonthAndYearAndCurrency(Long driverId, Integer month, Integer year, Currency currency);
}
