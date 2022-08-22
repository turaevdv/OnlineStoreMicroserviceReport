package ru.turaev.report.service;

import ru.turaev.report.model.DebitingInvoice;
import ru.turaev.report.model.GoodsAndQuantity;

import java.time.LocalDate;
import java.util.List;

public interface StorehouseReportService {
    List<DebitingInvoice> getDebitingReportByPeriod(long id, LocalDate begin, LocalDate end);
    List<GoodsAndQuantity> getDebitingProductsByPeriod(long id, LocalDate begin, LocalDate end);
}
