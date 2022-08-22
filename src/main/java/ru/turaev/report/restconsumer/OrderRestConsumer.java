package ru.turaev.report.restconsumer;

import ru.turaev.report.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderRestConsumer {
    List<Order> getOrderByPeriod(long id, LocalDate begin, LocalDate end);
}
