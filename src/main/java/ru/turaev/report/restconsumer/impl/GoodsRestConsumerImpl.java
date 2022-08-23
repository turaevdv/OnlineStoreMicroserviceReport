package ru.turaev.report.restconsumer.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.turaev.report.model.DebitingInvoice;
import ru.turaev.report.restconsumer.GoodsRestConsumer;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsRestConsumerImpl implements GoodsRestConsumer {
    private final WebClient.Builder webClientBuilder;

    @Value("${microservice.goods.name}")
    private String goodsServiceName;

    @Override
    public List<DebitingInvoice> getDebitingInvoiceByPeriod(long id, LocalDate begin, LocalDate end) {
        String path = getPathToService() + "pickup-point/" + id + "/debiting-invoices-by-period?begin=" + begin + "&end=" + end;

        return webClientBuilder.build()
                .get()
                .uri(path)
                .retrieve()
                .bodyToFlux(DebitingInvoice.class)
                .collectList()
                .block();
    }

    public String getPathToService() {
        return "http://" + goodsServiceName + "/api/v1/debiting-invoices/";
    }
}
