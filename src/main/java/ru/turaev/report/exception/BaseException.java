package ru.turaev.report.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public abstract class BaseException extends RuntimeException {
    private final HttpStatus status;
    private final LocalDateTime localDateTime;

    public BaseException(String message, HttpStatus httpStatus) {
        super(message);
        this.status = httpStatus;
        this.localDateTime = LocalDateTime.now();
    }
}