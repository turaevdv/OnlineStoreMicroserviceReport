package ru.turaev.report.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class Order {
    private List<AccountingAndQuantity> accountingAndQuantities;
    private LocalDate orderDate;
    private LocalTime orderTime;
    private long pickupPointId;
    private long userId;
    private int price;
}
