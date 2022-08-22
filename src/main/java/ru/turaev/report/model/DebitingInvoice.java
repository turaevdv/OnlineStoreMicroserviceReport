package ru.turaev.report.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class DebitingInvoice {
    private List<GoodsAndQuantity> goodsAndQuantities;
    private long storehouseId;
    private LocalDate invoiceDate;
    private LocalTime invoiceTime;
}
