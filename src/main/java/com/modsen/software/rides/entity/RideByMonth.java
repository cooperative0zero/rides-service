package com.modsen.software.rides.entity;

import com.modsen.software.rides.entity.enumeration.Currency;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "rides_by_month")
@Builder(toBuilder = true)
public class RideByMonth {
    @Id
    @SequenceGenerator(name = "sequence_id_auto_gen", allocationSize = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_id_auto_gen")
    @Column(name = "rm_id")
    private Long id;

    @Column(name = "rm_driver_id")
    private Long driverId;

    @Column(name = "rm_year")
    private Integer year;

    @Column(name = "rm_month")
    private Integer month;

    @Column(name = "rm_count")
    private Integer count;

    @Enumerated(EnumType.STRING)
    @Column(name = "rm_currency")
    private Currency currency;

    @Column(name = "rm_sum")
    private BigDecimal sum;
}
