package com.modsen.software.rides.service;

import com.modsen.software.rides.dto.RideRequest;
import com.modsen.software.rides.dto.RideResponse;
import com.modsen.software.rides.entity.enumeration.RideStatus;

import java.util.List;

public interface RideService {
    RideResponse getById(Long id);

    List<RideResponse> getAll(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    List<RideResponse> getAllByPassengerId(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    List<RideResponse> getAllByDriverId(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    RideResponse save(RideRequest request);

    RideResponse update(RideRequest request);

    RideResponse changeStatus(Long id, RideStatus newStatus);
}
