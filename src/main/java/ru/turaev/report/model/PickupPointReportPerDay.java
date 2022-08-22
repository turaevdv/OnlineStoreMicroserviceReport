package ru.turaev.report.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PickupPointReportPerDay {
    private LocalDate date;
    private List<AccountingAndQuantity> accountingAndQuantities;
    private long revenue;
}
