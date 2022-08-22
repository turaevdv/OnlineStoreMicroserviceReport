package ru.turaev.report.service;

import ru.turaev.report.model.Order;
import ru.turaev.report.model.PickupPointReport;

import java.time.LocalDate;
import java.util.List;

public interface PickupPointReportService {
    PickupPointReport getReportByPeriod(long id, LocalDate begin, LocalDate end);

    List<Order> getAllOrdersByPeriod(long id, LocalDate begin, LocalDate end);

    double getAverageReceiptByPeriod(long id, LocalDate begin, LocalDate end);

    double getNumbersOfOrderByPeriod(long id, LocalDate begin, LocalDate end);
}