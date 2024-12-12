package com.modsen.software.rides.service;

import com.modsen.software.rides.dto.RideReportRequest;
import com.modsen.software.rides.entity.Ride;
import com.modsen.software.rides.exception.BaseCustomException;
import com.modsen.software.rides.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RideReportService {

    private RideRepository rideRepository;
    private EmailService emailService;

    @SneakyThrows
    public void generateRideReport(Long driverId, Long passengerId, OffsetDateTime fromDate, OffsetDateTime toDate,
                                   String[] statusTypes, String email, int maxRowsCount) {

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             Workbook workbook = new XSSFWorkbook()) {

            List<Ride> rides = rideRepository.findAllByParameters(driverId, passengerId, fromDate,
                    toDate, statusTypes, PageRequest.ofSize(maxRowsCount)).toList();

            Sheet sheet = workbook.createSheet("Rides report");

            Row headerRow = sheet.createRow(0);
            var cells = new String[]{
                    "Driver ID",
                    "Passenger ID",
                    "Creation date",
                    "Completion date",
                    "Departure address",
                    "Destination address",
                    "Ride's status"
            };

            for (int i = 0; i < cells.length; i++) {
                headerRow.createCell(i).setCellValue(cells[i]);
            }

            int rowNum = 1;
            for (Ride ride : rides) {
                Row row = sheet.createRow(rowNum);

                row.createCell(0).setCellValue(ride.getDriverId());
                row.createCell(1).setCellValue(ride.getPassengerId());
                row.createCell(2).setCellValue(ride.getCreationDate().toString());
                row.createCell(3).setCellValue(ride.getCompletionDate().toString());
                row.createCell(4).setCellValue(ride.getDepartureAddress());
                row.createCell(5).setCellValue(ride.getDestinationAddress());
                row.createCell(6).setCellValue(ride.getRideStatus().toString());

                rowNum++;
            }

            workbook.write(outputStream);

            emailService.sendReport(email, outputStream.toByteArray());
        } catch (IOException e) {
            throw new BaseCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Report generation failed");
        }
    }
}

