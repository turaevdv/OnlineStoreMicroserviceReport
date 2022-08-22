package ru.turaev.report.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.turaev.report.model.Order;
import ru.turaev.report.model.PickupPointReport;
import ru.turaev.report.service.PickupPointReportService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/reports/pickup-points")
@RequiredArgsConstructor
public class PickupPointReportController {
    private final PickupPointReportService pickupPointReportService;

    /**Возвращает отчет по покупкам пункта выдачи за каждый день этого промежутка*/
    @GetMapping("/{id}/reports")
    public PickupPointReport getReportByPeriod(@PathVariable long id,
                                               @RequestParam("begin") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate begin,
                                               @RequestParam("end") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate end) {
        return pickupPointReportService.getReportByPeriod(id, begin, end);
    }

    /**Возвращает список заказов пункта выдачи за каждый день этого промежутка*/
    @GetMapping("/{id}/orders")
    public List<Order> getAllOrdersByPeriod(@PathVariable long id,
                                    @RequestParam("begin") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate begin,
                                    @RequestParam("end") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate end) {
        return pickupPointReportService.getAllOrdersByPeriod(id, begin, end);
    }

    /**Возвращает средний чек пункта выдачи за период времени*/
    @GetMapping("/{id}/average-receipt")
    public double getAverageReceiptByPeriod(@PathVariable long id,
                                    @RequestParam("begin") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate begin,
                                    @RequestParam("end") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate end) {
        return pickupPointReportService.getAverageReceiptByPeriod(id, begin, end);
    }

    /**Возвращает количество заказов пункта выдачи за период времени*/
    @GetMapping("/{id}/numbers-of-order")
    public double getNumbersOfOrderByPeriod(@PathVariable long id,
                                            @RequestParam("begin") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate begin,
                                            @RequestParam("end") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate end) {
        return pickupPointReportService.getNumbersOfOrderByPeriod(id, begin, end);
    }
}
