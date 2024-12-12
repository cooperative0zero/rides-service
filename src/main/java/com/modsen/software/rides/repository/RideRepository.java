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

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    Page<Ride> findAllByPassengerId(Long passengerId, Pageable pageable);
    Page<Ride> findAllByDriverId(Long driverId, Pageable pageable);

    @Modifying
    @Query("UPDATE Ride r SET r.rideStatus = :status WHERE r.id = :id")
    int changeStatus(@Param("id") Long id, @Param("status") RideStatus status);

    @Query("SELECT e FROM YourEntity e WHERE " +
            "(:driverId IS NULL OR e.driverId = :driverId) AND " +
            "(:passengerId IS NULL OR e.passengerId = :passengerId) AND " +
            "(:fromDate IS NULL OR e.creationDate >= :fromDate) AND " +
            "(:toDate IS NULL OR e.creationDate <= :toDate) AND " +
            "(:statusTypes IS NULL OR e.rideStatus IN :statusTypes)")
    Page<Ride> findAllByParameters(
            @Param("driverId") Long driverId,
            @Param("passengerId") Long passengerId,
            @Param("fromDate") OffsetDateTime fromDate,
            @Param("toDate") OffsetDateTime toDate,
            @Param("statusTypes") String[] statusTypes,
            Pageable pageable);
}
