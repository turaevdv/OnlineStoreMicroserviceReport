package ru.turaev.report.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.turaev.report.exception.OrderNotFoundException;
import ru.turaev.report.model.AccountingAndQuantity;
import ru.turaev.report.model.Order;
import ru.turaev.report.model.PickupPointReport;
import ru.turaev.report.model.PickupPointReportPerDay;
import ru.turaev.report.restconsumer.OrderRestConsumer;
import ru.turaev.report.service.PickupPointReportService;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class PickupPointReportServiceImpl implements PickupPointReportService {
    private final OrderRestConsumer orderRestConsumer;

    @Override
    public PickupPointReport getReportByPeriod(long id, LocalDate begin, LocalDate end) {
        log.info("Create an pickup point report for pickup point with id = {} from {} to {}", id, begin, end);
        List<Order> orders = getOrderByPeriod(id, begin, end);

        PickupPointReport pickupPointReport = new PickupPointReport();
        List<PickupPointReportPerDay> pickupPointReportPerDays = new ArrayList<>();
        int revenueForPeriod = 0;

        for (LocalDate date = begin; date.isBefore(end.plusDays(1)); date = date.plusDays(1)) {
            final LocalDate currentDay = date;
            long revenuePerDay = 0;
            List<Order> ordersPerDay = orders.stream()
                    .filter(order -> order.getOrderDate().equals(currentDay))
                    .collect(Collectors.toList());

            if (ordersPerDay.isEmpty()) {
                continue;
            }

            List<AccountingAndQuantity> reportPerDayAccountingAndQuantities = new ArrayList<>();
            for (Order order : ordersPerDay) {
                for (AccountingAndQuantity orderAccountingAndQuantity : order.getAccountingAndQuantities()) {
                    reportPerDayAccountingAndQuantities
                            .stream()
                            .filter(a -> a.getAccountingId() == orderAccountingAndQuantity.getAccountingId())
                            .findFirst()
                            .ifPresentOrElse(a -> a.setQuantity(a.getQuantity() + orderAccountingAndQuantity.getQuantity()),
                            () -> reportPerDayAccountingAndQuantities.add(new AccountingAndQuantity(orderAccountingAndQuantity.getAccountingId(), orderAccountingAndQuantity.getQuantity())));
                }
                revenuePerDay += order.getPrice();
            }

            PickupPointReportPerDay pickupPointReportPerDay = new PickupPointReportPerDay();
            pickupPointReportPerDay.setDate(currentDay);
            pickupPointReportPerDay.setAccountingAndQuantities(reportPerDayAccountingAndQuantities);
            pickupPointReportPerDay.setRevenue(revenuePerDay);
            pickupPointReportPerDays.add(pickupPointReportPerDay);
            revenueForPeriod += revenuePerDay;
        }

        pickupPointReport.setPickupPointId(id);
        pickupPointReport.setPickupPointReportPerDay(pickupPointReportPerDays);
        pickupPointReport.setRevenueForPeriod(revenueForPeriod);
        log.info("The report was created successfully");
        return pickupPointReport;
    }

    @Override
    public List<Order> getAllOrdersByPeriod(long id, LocalDate begin, LocalDate end) {
        log.info("Try to get all orders for pickup point with id = {} from {} to {}", id, begin, end);
        return getOrderByPeriod(id, begin, end);
    }

    @Override
    public double getAverageReceiptByPeriod(long id, LocalDate begin, LocalDate end) {
        log.info("Try to calculate the average receipt for pickup point with id = {} from {} to {}", id, begin, end);
        List<Order> orders = getOrderByPeriod(id, begin, end);
        if (orders.size() == 0) {
            log.info("There were no orders from {} to {} in the pickup point with id = {}", begin, end, id);
            return 0;
        }
        int revenue = orders.stream().flatMapToInt(order -> IntStream.of(order.getPrice())).sum();
        double averageReceipt = (double) revenue / orders.size();
        log.info("The average receipt for pickup point with id = {} from {} to {} is {}", id, begin, end, averageReceipt);
        return averageReceipt;
    }

    @Override
    public double getNumbersOfOrderByPeriod(long id, LocalDate begin, LocalDate end) {
        return getOrderByPeriod(id, begin, end).size();
    }

    private void checkPeriod(LocalDate begin, LocalDate end) {
        if (begin.isAfter(end)) {
            throw new DateTimeException("The beginning of the period is later than the end");
        }
    }

    private List<Order> getOrderByPeriod(long id, LocalDate begin, LocalDate end) {
        checkPeriod(begin, end);
        log.info("Try to get order data");
        try {
            return orderRestConsumer.getOrderByPeriod(id, begin, end);
        } catch (Exception e) {
            throw new OrderNotFoundException("Error when trying to get order data");
        }
    }
}
