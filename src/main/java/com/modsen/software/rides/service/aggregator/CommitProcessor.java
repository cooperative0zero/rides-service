package com.modsen.software.rides.service.aggregator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.software.rides.entity.Ride;
import com.modsen.software.rides.entity.RideByMonth;
import com.modsen.software.rides.entity.enumeration.Currency;
import com.modsen.software.rides.entity.enumeration.RideStatus;
import com.modsen.software.rides.repository.RideByMonthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class CommitProcessor implements ItemProcessor<List<String>, List<String>> {

    private final ObjectMapper mapper;
    private final RideByMonthRepository repository;

    @Override
    @Transactional
    public List<String> process(List<String> items) throws Exception {
        for (String action : items) {
            JsonNode rootNode = mapper.readTree(action);

            String operation = rootNode.get("op").asText();
            JsonNode before = rootNode.get("before");
            JsonNode after = rootNode.get("after");

            if (after.get("r_status").asText().equalsIgnoreCase(RideStatus.COMPLETED.toString())) {
                if (operation.contentEquals("u")) {
                    rideChangedHandler(before, after);
                } else if (operation.contentEquals("c")) {
                    rideCompletedHandler(after);
                }
            }
        }

        return items;
    }

    private void rideCompletedHandler(JsonNode after) {
        long driverId = after.get("r_driver_id").asLong();
        long milliseconds = after.get("r_creation_date").asLong() / 1_000;
        OffsetDateTime dateTime = Instant.ofEpochMilli(milliseconds).atOffset(ZoneOffset.UTC);
        Currency currency = Currency.valueOf(after.get("r_currency").asText());
        BigDecimal price = after.get("r_cost").decimalValue();

        var rideByMonth = repository
                .findByDriverIdAndMonthAndYearAndCurrency(driverId, dateTime.getMonthValue(), dateTime.getYear(), currency);
        RideByMonth result;

        if (rideByMonth.isEmpty()) {
            result = RideByMonth.builder()
                    .sum(price)
                    .count(1)
                    .month(dateTime.getMonthValue())
                    .year(dateTime.getYear())
                    .currency(currency)
                    .driverId(driverId)
                    .build();
        } else {
            RideByMonth oldValue = rideByMonth.get();

            oldValue.setCount(oldValue.getCount() + 1);
            oldValue.setSum(oldValue.getSum().add(price));

            result = oldValue;
        }
        repository.save(result);
    }

    private void rideChangedHandler(JsonNode before, JsonNode after) {
        if (!before.get("r_status").asText().equalsIgnoreCase(RideStatus.COMPLETED.toString()))
            rideCompletedHandler(after);
        else {
            long driverId = after.get("r_driver_id").asLong();
            long milliseconds = after.get("r_creation_date").asLong() / 1_000;
            OffsetDateTime dateTime = Instant.ofEpochMilli(milliseconds).atOffset(ZoneOffset.UTC);
            Currency currency = Currency.valueOf(before.get("r_currency").asText());
            BigDecimal oldPrice = before.get("r_cost").decimalValue();

            RideByMonth rideByMonth = repository
                    .findByDriverIdAndMonthAndYearAndCurrency(driverId, dateTime.getMonthValue(), dateTime.getYear(), currency).get();

            BigDecimal priceDelta = after.get("r_cost").decimalValue().subtract(oldPrice);

            if (!before.get("r_currency").asText().equalsIgnoreCase(after.get("r_currency").asText())) {
                rideByMonth.setCount(rideByMonth.getCount() - 1);
                rideByMonth.setSum(rideByMonth.getSum().subtract(oldPrice));

                rideCompletedHandler(after);
            } else {
                rideByMonth.setSum(rideByMonth.getSum().add(priceDelta));
            }

            repository.save(rideByMonth);
        }
    }
}
