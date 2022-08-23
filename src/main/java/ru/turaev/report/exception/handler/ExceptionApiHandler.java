package ru.turaev.report.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.turaev.report.exception.BaseException;
import ru.turaev.report.exception.DebitingInvoiceNotFoundException;
import ru.turaev.report.exception.OrderNotFoundException;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler({OrderNotFoundException.class, DebitingInvoiceNotFoundException.class})
    public ResponseEntity<?> handleNotFoundException(BaseException ex) {
        log.warn("An error has occurred. Error message - {}", ex.getMessage());
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", ex.getMessage());
        responseMap.put("exceptionTime", ex.getLocalDateTime());
        return new ResponseEntity<>(responseMap, ex.getStatus());
    }

    @ExceptionHandler({DateTimeException.class})
    public ResponseEntity<?> handleDateTimeException(DateTimeException ex) {
        log.warn("An error has occurred. Error message - {}", ex.getMessage());
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", ex.getMessage());
        responseMap.put("exceptionTime", LocalDateTime.now());
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }
}
