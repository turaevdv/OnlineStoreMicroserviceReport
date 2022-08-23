package ru.turaev.report.restconsumer;

import ru.turaev.report.model.DebitingInvoice;

import java.time.LocalDate;
import java.util.List;

public interface GoodsRestConsumer {
    List<DebitingInvoice> getDebitingInvoiceByPeriod(long id, LocalDate begin, LocalDate end);
}
