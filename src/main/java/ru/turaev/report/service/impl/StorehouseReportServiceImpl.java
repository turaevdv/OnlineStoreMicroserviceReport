package ru.turaev.report.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.turaev.report.exception.DebitingInvoiceNotFoundException;
import ru.turaev.report.model.DebitingInvoice;
import ru.turaev.report.model.GoodsAndQuantity;
import ru.turaev.report.restconsumer.GoodsRestConsumer;
import ru.turaev.report.service.StorehouseReportService;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorehouseReportServiceImpl implements StorehouseReportService {
    private final GoodsRestConsumer goodsRestConsumer;

    @Override
    public List<DebitingInvoice> getDebitingReportByPeriod(long id, LocalDate begin, LocalDate end) {
        return getDebitingInvoiceByPeriod(id, begin, end);
    }

    @Override
    public List<GoodsAndQuantity> getDebitingProductsByPeriod(long id, LocalDate begin, LocalDate end) {
        log.info("Try to get all debiting products in the storehouse with id = {} from {} to {}", id, begin, end);
        List<DebitingInvoice> debitingInvoices = getDebitingInvoiceByPeriod(id, begin, end);
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
        log.info("Debiting products in the storehouse with id = {} from {} to {} have been received", id, begin, end);
        return goodsAndQuantities;
    }

    private void checkPeriod(LocalDate begin, LocalDate end) {
        if (begin.isAfter(end)) {
            throw new DateTimeException("The beginning of the period is later than the end");
        }
    }

    private List<DebitingInvoice> getDebitingInvoiceByPeriod(long id, LocalDate begin, LocalDate end) {
        checkPeriod(begin, end);
        log.info("Try to get debiting invoice data");
        try {
            return goodsRestConsumer.getDebitingInvoiceByPeriod(id, begin, end);
        } catch (Exception e) {
            throw new DebitingInvoiceNotFoundException("Error when trying to get debiting invoice data");
        }
    }
}
