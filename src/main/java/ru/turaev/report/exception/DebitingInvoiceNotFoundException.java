package ru.turaev.report.exception;

import org.springframework.http.HttpStatus;

public class DebitingInvoiceNotFoundException extends BaseException {

    public DebitingInvoiceNotFoundException() {
        this("Debiting invoice not found");
    }

    public DebitingInvoiceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}