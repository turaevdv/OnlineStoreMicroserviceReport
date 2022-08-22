package ru.turaev.report.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

@Service
@RequiredArgsConstructor
public class PickupPointReportServiceImpl implements PickupPointReportService {
    private final OrderRestConsumer orderRestConsumer;

    @Override
    public PickupPointReport getReportByPeriod(long id, LocalDate begin, LocalDate end) {
        checkPeriod(begin, end);
        PickupPointReport pickupPointReport = new PickupPointReport();
        List<PickupPointReportPerDay> pickupPointReportPerDays = new ArrayList<>();
        int revenueForPeriod = 0;

        List<Order> orders = orderRestConsumer.getOrderByPeriod(id, begin, end);
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
        return pickupPointReport;
    }

    @Override
    public List<Order> getAllOrdersByPeriod(long id, LocalDate begin, LocalDate end) {
        checkPeriod(begin, end);
        return orderRestConsumer.getOrderByPeriod(id, begin, end);
    }

    @Override
    public double getAverageReceiptByPeriod(long id, LocalDate begin, LocalDate end) {
        checkPeriod(begin, end);
        List<Order> orders = orderRestConsumer.getOrderByPeriod(id, begin, end);
        if (orders.size() == 0) {
            return 0;
        }
        int revenue = orders.stream().flatMapToInt(order -> IntStream.of(order.getPrice())).sum();
        return (double) revenue / orders.size();
    }

    @Override
    public double getNumbersOfOrderByPeriod(long id, LocalDate begin, LocalDate end) {
        checkPeriod(begin, end);
        return orderRestConsumer.getOrderByPeriod(id, begin, end).size();
    }

    private void checkPeriod(LocalDate begin, LocalDate end) {
        if (begin.isAfter(end)) {
            throw new DateTimeException("The beginning of the period is later than the end");
        }
    }
}
