package ru.turaev.report.model;

import lombok.Data;

import java.util.List;

@Data
public class PickupPointReport {
    private long pickupPointId;
    private List<PickupPointReportPerDay> pickupPointReportPerDay;
    private long revenueForPeriod;
}
