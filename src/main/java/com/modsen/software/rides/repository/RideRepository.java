package com.modsen.software.rides.repository;

import com.modsen.software.rides.entity.Ride;
import com.modsen.software.rides.entity.enumeration.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    Page<Ride> findAllByPassengerId(Long passengerId, Pageable pageable);
    Page<Ride> findAllByDriverId(Long driverId, Pageable pageable);

    @Modifying
    @Query("UPDATE Ride r SET r.rideStatus = :status WHERE r.id = :id")
    int changeStatus(@Param("id") Long id, @Param("status") RideStatus status);
}
