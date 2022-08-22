package ru.turaev.report.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.turaev.report.model.DebitingInvoice;
import ru.turaev.report.model.GoodsAndQuantity;
import ru.turaev.report.service.StorehouseReportService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StorehouseReportServiceImpl implements StorehouseReportService {
    @Override
    public List<DebitingInvoice> getDebitingReportByPeriod(long id, LocalDate begin, LocalDate end) {
        return null;
    }

    @Override
    public List<GoodsAndQuantity> getDebitingProductsByPeriod(long id, LocalDate begin, LocalDate end) {
        return null;
    }
}
