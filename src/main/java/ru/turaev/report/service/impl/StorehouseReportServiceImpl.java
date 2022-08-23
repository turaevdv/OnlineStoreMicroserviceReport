package ru.turaev.report.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.turaev.report.model.DebitingInvoice;
import ru.turaev.report.model.GoodsAndQuantity;
import ru.turaev.report.restconsumer.GoodsRestConsumer;
import ru.turaev.report.service.StorehouseReportService;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StorehouseReportServiceImpl implements StorehouseReportService {
    private final GoodsRestConsumer goodsRestConsumer;

    @Override
    public List<DebitingInvoice> getDebitingReportByPeriod(long id, LocalDate begin, LocalDate end) {
        checkPeriod(begin, end);
        return goodsRestConsumer.getDebitingInvoiceByPeriod(id, begin, end);
    }

    @Override
    public List<GoodsAndQuantity> getDebitingProductsByPeriod(long id, LocalDate begin, LocalDate end) {
        checkPeriod(begin, end);
        List<DebitingInvoice> debitingInvoices = goodsRestConsumer.getDebitingInvoiceByPeriod(id, begin, end);
        List<GoodsAndQuantity> goodsAndQuantities = new ArrayList<>();

        for (DebitingInvoice debitingInvoice : debitingInvoices) {
            for (GoodsAndQuantity goodsAndQuantity : debitingInvoice.getGoodsAndQuantities()) {
                goodsAndQuantities.stream()
                        .filter(g -> g.getProductId() == goodsAndQuantity.getProductId())
                        .findFirst()
                        .ifPresentOrElse(g -> g.setQuantity(g.getQuantity() + goodsAndQuantity.getQuantity()),
                                () -> goodsAndQuantities.add(new GoodsAndQuantity(goodsAndQuantity.getProductId(), goodsAndQuantity.getQuantity())));
            }
        }
        return goodsAndQuantities;
    }

    private void checkPeriod(LocalDate begin, LocalDate end) {
        if (begin.isAfter(end)) {
            throw new DateTimeException("The beginning of the period is later than the end");
        }
    }
}
