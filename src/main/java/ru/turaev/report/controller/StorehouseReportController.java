package ru.turaev.report.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.turaev.report.model.DebitingInvoice;
import ru.turaev.report.model.GoodsAndQuantity;
import ru.turaev.report.service.StorehouseReportService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/reports/storehouses")
@RequiredArgsConstructor
public class StorehouseReportController {
    private final StorehouseReportService storehouseReportService;

    /**Возвращает список подтвержденных накладных для списания товара из этого склада за каждый день этого промежутка*/
    @GetMapping("/{id}/debiting")
    public List<DebitingInvoice> getDebitingReportByPeriod(@PathVariable long id,
                                                           @RequestParam("begin") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate begin,
                                                           @RequestParam("end") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate end) {
        return storehouseReportService.getDebitingReportByPeriod(id, begin, end);
    }

    /**Возвращает список списанных товаров из этого склада за каждый день этого промежутка*/
    @GetMapping("/{id}/debiting-invoice")
    public List<GoodsAndQuantity> getDebitingProductsByPeriod(@PathVariable long id,
                                                              @RequestParam("begin") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate begin,
                                                              @RequestParam("end") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate end) {
        return storehouseReportService.getDebitingProductsByPeriod(id, begin, end);
    }
}
