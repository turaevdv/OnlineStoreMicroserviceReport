package ru.turaev.report.restconsumer.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.turaev.report.model.Order;
import ru.turaev.report.restconsumer.OrderRestConsumer;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderRestConsumerImpl implements OrderRestConsumer {
    private final WebClient.Builder webClientBuilder;

    @Value("${microservice.order.name}")
    private String orderServiceName;

    @Override
    public List<Order> getOrderByPeriod(long id, LocalDate begin, LocalDate end) {
        String path = getPathToService() + "reports/pickup-points/" + id + "/orders?begin=" + begin + "&end=" + end;

        return webClientBuilder.build()
                .get()
                .uri(path)
                .retrieve()
                .bodyToFlux(Order.class)
                .collectList()
                .block();
    }

    public String getPathToService() {
        return "http://" + orderServiceName + "/api/v1/orders/";
    }
}
