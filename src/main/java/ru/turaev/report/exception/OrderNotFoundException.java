package ru.turaev.report.exception;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends BaseException {

    public OrderNotFoundException() {
        this("Order not found");
    }

    public OrderNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}